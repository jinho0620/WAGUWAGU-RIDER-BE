package com.example.waguwagu.service;

import ch.hsr.geohash.BoundingBox;
import ch.hsr.geohash.GeoHash;
import com.example.waguwagu.domain.dto.request.RiderAssignRequest;
import com.example.waguwagu.domain.dto.response.RiderAssignResponse;
import com.example.waguwagu.domain.entity.DeliveryRequest;
import com.example.waguwagu.domain.entity.Rider;
import com.example.waguwagu.domain.type.Transportation;
import com.example.waguwagu.global.exception.RiderNotActiveException;
import com.example.waguwagu.global.repository.DeliveryRequestRedisRepository;
import com.example.waguwagu.global.util.GeoHashUtil;
import com.example.waguwagu.kafka.KafkaStatus;
import com.example.waguwagu.kafka.dto.KafkaDeliveryRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metric;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;

import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/* It is inefficient to search the entire DB every time a request comes in.
   Instead, it is most efficient to filter riders and send them requests. -> use socket
1. If within the current activity area, send GPS latitude and longitude. @front -> Check if the rider is within the activity area
2. Fetch the list from the DB @back
3. Validate if the store is within the activity area @back
4. Filter only those that match the rider's transportation mode -> By this point, we've filtered requests that I have the right to deliver @back
5. Calculate the distance between the filtered requests and the rider's location @back
6. Send the store name/address, delivery fee, delivery target time, distance between the store and rider, and distance from the store to the customer to the rider @back
*/
@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryRequestServiceImpl implements DeliveryRequestService {
    private final DeliveryRequestRedisRepository deliveryRequestRedisRepository;
    private final RiderService riderService;
    private final RedisTemplate<String, String> redisTemplate;
    private static final String REDIS_GEO_KEY = "points";
    private static final String REDIS_GEO_MEMBER_RIDER = "rider";
    private static final String REDIS_GEO_MEMBER_STORE = "store";
    private static final int MAX_DELIVERABLE_DISTANCE = 5;
    private static final String REDIS_HASH_KEY = "riderLocations";
    private static final int GEO_HASH_PRECISION = 7; // 150m X 150m

    public List<DeliveryRequest> findNearByOrders(Long riderId, RiderAssignRequest req) throws JsonProcessingException {
        Rider rider = riderService.getById(riderId);
        if (rider.isNotActive()) throw new RiderNotActiveException(); // Check if the rider is active
        GeoHash centerGeoHash = GeoHash.withCharacterPrecision(req.latitude(), req.longitude(), GEO_HASH_PRECISION);
        // Create a bounding box of 150m X 150m centered on the rider's location
        BoundingBox searchArea = centerGeoHash.getBoundingBox();
        // Expand the bounding box by an additional 5km from the rider's location
        BoundingBox expandedBox = GeoHashUtil.expandBoundingBox(searchArea, req.latitude());
        // Extract geoHash values at 150m intervals within the bounding box
        List<String> nearByHashes = GeoHashUtil.coverBoundingBox(expandedBox, GEO_HASH_PRECISION);
        log.info(nearByHashes.toString());
        List<DeliveryRequest> nearbyOrders = new ArrayList<>();
        // Fetch all data with the key REDIS_HASH_KEY (rider_locations) @redis
        Map<Object, Object> storedDeliveryRequests = redisTemplate.opsForHash().entries(REDIS_HASH_KEY);
        ObjectMapper objectMapper = new ObjectMapper();
        // Check each delivery request stored in redis
        for (Map.Entry<Object, Object> entry : storedDeliveryRequests.entrySet()) {
            String storedGeoHash = (String) entry.getValue(); // Get the geohash value of the delivery request
            log.info(entry.toString());
            // Validate if the delivery request's geohash is within the rider's 5km radius geohash range
            if (nearByHashes.contains(storedGeoHash)) {
                DeliveryRequest deliveryRequest = objectMapper.readValue(entry.getKey().toString(), DeliveryRequest.class);
                // Extract the district of the store (e.g., Nowon-gu), example storeAddress: "Seoul Nowon-gu Dongil-ro"
                String[] storeAddress = deliveryRequest.getStoreAddress().split(" ");
                String storeDistrict = storeAddress[1];
                // Validate if the store is within the rider's activity area, if the delivery request includes the rider's transportation, and if the rider is already assigned
                if (deliveryRequest.isNotAssigned() && rider.getActivityArea().contains(storeDistrict)
                        && deliveryRequest.getTransportations().contains(rider.getTransportation())) {
                    // Calculate the delivery cost burdened by the store (considering transportation mode and distance from store to customer)
                    int costByDistance = (int) (rider.getTransportation().costPerKm * deliveryRequest.getDistanceFromStoreToCustomer());
                    int totalCost = deliveryRequest.getDeliveryPay() + costByDistance;
                    deliveryRequest.setDeliveryPay(totalCost);
                    nearbyOrders.add(deliveryRequest);
                }
            }
        }
        return nearbyOrders;
    }

    public List<RiderAssignResponse> assignRider(Long riderId, RiderAssignRequest req) {
        Rider rider = riderService.getById(riderId);
        if (rider.isNotActive()) throw new RiderNotActiveException(); // Check if the rider is active
        List<DeliveryRequest> deliveryRequests = deliveryRequestRedisRepository.findAll();
        log.info(deliveryRequests.toString());
        String key = REDIS_GEO_KEY;
        Metric metric = RedisGeoCommands.DistanceUnit.KILOMETERS;
        Point riderLocation = new Point(req.longitude(), req.latitude());
        GeoOperations<String, String> geoOperations = redisTemplate.opsForGeo();
        geoOperations.add(key, riderLocation, REDIS_GEO_MEMBER_RIDER);
        List<RiderAssignResponse> list = new ArrayList<>();

        for (DeliveryRequest deliveryRequest : deliveryRequests) {
            // Extract the district of the store (e.g., Nowon-gu)
            String[] storeAddress = deliveryRequest.getStoreAddress().split(" ");
            String storeDistrict = storeAddress[1];

            // Validate if the store is within the rider's activity area, if the delivery request includes the rider's transportation, and if the rider is already assigned
            if(deliveryRequest.isNotAssigned() && rider.getActivityArea().contains(storeDistrict)
                    && deliveryRequest.getTransportations().contains(rider.getTransportation())) {
                // If valid, add to the request list
                geoOperations.add(
                        key,
                        new Point(deliveryRequest.getStoreLongitude(), deliveryRequest.getStoreLatitude()),
                        REDIS_GEO_MEMBER_STORE);
                Distance distance = geoOperations.distance(key, REDIS_GEO_MEMBER_RIDER, REDIS_GEO_MEMBER_STORE, metric);

                // If the distance between the rider and the store is within 5km, assign the request
                if (distance.getValue() <= MAX_DELIVERABLE_DISTANCE) {
                    // Calculate the delivery cost burdened by the store (considering transportation mode and distance from store to customer)
                    int costByDistance = (int) (rider.getTransportation().costPerKm * deliveryRequest.getDistanceFromStoreToCustomer());
                    int totalCost = deliveryRequest.getDeliveryPay() + costByDistance;
                    RiderAssignResponse response = RiderAssignResponse.from(deliveryRequest, distance, totalCost);
                    list.add(response);
                }
            }
        }
//         Return the request list
        return list;
    }

    @KafkaListener(topics = "order-topic", id = "delivery")
    public void saveOrder(KafkaStatus<KafkaDeliveryRequestDto> dto) throws JsonProcessingException {
        if (dto.status().equals("insert")) {
            // Limit available transportation modes based on the distance from the store to the customer
            List<Transportation> transportations = Transportation
                    .chooseTransportationByDistance(dto.data().distanceFromStoreToCustomer());
            DeliveryRequest deliveryRequest = dto.data().toEntity(transportations);
            // Create a 150m X 150m grid centered on the store
            GeoHash geoHash = GeoHash.withCharacterPrecision(
                    dto.data().storeLatitude(),
                    dto.data().storeLongitude(),
                    GEO_HASH_PRECISION);
            String geoHashString = geoHash.toBase32();
            ObjectMapper objectMapper = new ObjectMapper();
            String deliveryRequestJson = objectMapper.writeValueAsString(deliveryRequest);
            redisTemplate.opsForHash().put(REDIS_HASH_KEY, deliveryRequestJson, geoHashString);
        }
    }

    @Override
    public void deleteDeliveryRequest(String deliveryRequestJson) {
        redisTemplate.opsForHash().delete(REDIS_HASH_KEY, deliveryRequestJson);
    }

}
