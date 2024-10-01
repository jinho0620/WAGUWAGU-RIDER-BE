package com.example.waguwagu.service;

import ch.hsr.geohash.BoundingBox;
import ch.hsr.geohash.GeoHash;
import com.example.waguwagu.domain.dto.request.RiderAssignRequest;
import com.example.waguwagu.domain.dto.response.NearByOrderResponse;
import com.example.waguwagu.domain.dto.response.RiderAssignResponse;
import com.example.waguwagu.domain.entity.DeliveryRequest;
import com.example.waguwagu.domain.entity.Rider;
import com.example.waguwagu.domain.type.Transportation;
import com.example.waguwagu.global.exception.DeliveryRequestNotFoundException;
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
import org.springframework.util.StopWatch;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor

/* 요청이 올 때마다 DB 전체를 뒤지는 것은 비효율적이다. 요청이 들어올 때마다, 라이더들을 추려내서 쏴주는 것이 가장 효율적이다. -> socket 이용
1. 현재 활동 범위 안에 있다면 gps로 위, 경도를 쏜다. @front -> 라이더가 활동 범위 안에 있는지
2. DB에서 목록을 가져옴 @back
3. 가게가 활동 범위 안에 있는지 검증 @back
4. 이동 수단이 라이더의 것과 맞는 것만 추려냄 -> 여기까지 진행하면 내가 배달할 권리가 있는 것들을 추려냄 @back
5. 걸러낸 각 요청들에서 라이더~가게 사이의 거리를 계산 @back
6. 가게 이름/주소, 배달료, 배달 목표 시간, 가게~라이더 사이 거리, 가게~고객 거리를 라이더한테 줌 @back
*/
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

    public List<RiderAssignResponse> assignRider(Long riderId, RiderAssignRequest req) {
        Rider rider = riderService.getById(riderId);
        if (rider.isNotActive()) throw new RiderNotActiveException(); // rider가 활성화 상태인지 확인
        List<DeliveryRequest> deliveryRequests = deliveryRequestRedisRepository.findAll();
        log.info(deliveryRequests.toString());
        String key = REDIS_GEO_KEY;
        Metric metric = RedisGeoCommands.DistanceUnit.KILOMETERS;
        Point riderLocation = new Point(req.longitude(), req.latitude());
        GeoOperations<String, String> geoOperations = redisTemplate.opsForGeo();
        geoOperations.add(key, riderLocation, REDIS_GEO_MEMBER_RIDER);
        List<RiderAssignResponse> list = new ArrayList<>();

        for (DeliveryRequest deliveryRequest : deliveryRequests) {
            // 가게의 구(ex. 노원구) 꺼내기
            String[] storeAddress = deliveryRequest.getStoreAddress().split(" ");
            String storeDistrict = storeAddress[1];

            // 가게가 내 활동 범위에 있는지, 배달 요청에 내 이동수단이 포함되는지 그리고 이미 라이더가 배정되었는지 검증
            if(deliveryRequest.isNotAssigned() && rider.getActivityArea().contains(storeDistrict)
                    && deliveryRequest.getTransportations().contains(rider.getTransportation())) {
                // 맞다면 요청 목록에 추가
                geoOperations.add(
                        key,
                        new Point(deliveryRequest.getStoreLongitude(), deliveryRequest.getStoreLatitude()),
                        REDIS_GEO_MEMBER_STORE);
                Distance distance = geoOperations.distance(key, REDIS_GEO_MEMBER_RIDER, REDIS_GEO_MEMBER_STORE, metric);

                // 라이더 ~ 가게 거리가 5km 이내에 있다면 배정
                if (distance.getValue() <= MAX_DELIVERABLE_DISTANCE) {
                    // 가게 부담 배달비 계산 (이동수단과 가게 ~ 고객 거리 고려)
                    int costByDistance = (int) (rider.getTransportation().costPerKm * deliveryRequest.getDistanceFromStoreToCustomer());
                    int totalCost = deliveryRequest.getDeliveryPay() + costByDistance;
                    RiderAssignResponse response = RiderAssignResponse.from(deliveryRequest, distance, totalCost);
                    list.add(response);
                }
            }
        }
//         요청 목록 return
        return list;
    }

    @KafkaListener(topics = "order-topic", id = "delivery")
    public void saveOrder(KafkaStatus<KafkaDeliveryRequestDto> dto) throws JsonProcessingException {
        if (dto.status().equals("insert")) {
            // 가게 ~ 고객 거리에 따라 배달 가능한 이동 수단 제한
            List<Transportation> transportations = Transportation
                    .chooseTransportationByDistance(dto.data().distanceFromStoreToCustomer());
            DeliveryRequest deliveryRequest = dto.data().toEntity(transportations);
            // 가게를 중심으로 150m X 150m 의 격자를 만듦
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

    public List<NearByOrderResponse> findNearByOrders(Long riderId, RiderAssignRequest req) throws JsonProcessingException {
        Rider rider = riderService.getById(riderId);
        if (rider.isNotActive()) throw new RiderNotActiveException(); // rider가 활성화 상태인지 확인
        GeoHash centerGeoHash = GeoHash.withCharacterPrecision(req.latitude(), req.longitude(), GEO_HASH_PRECISION);
        // 라이더 기준 가로, 세로 150m X 150m 로 격자(bounding box) 생성
        BoundingBox searchArea = centerGeoHash.getBoundingBox();
        // 라이더 기준 가로, 세로 5km 추가하여 bounding box 확장
        BoundingBox expandedBox = GeoHashUtil.expandBoundingBox(searchArea, req.latitude());
        // bounding box 안에서 150m 간격으로 geoHash 추출
        List<String> nearByHashes = GeoHashUtil.coverBoundingBox(expandedBox, GEO_HASH_PRECISION);
        log.info(nearByHashes.toString());
        List<NearByOrderResponse> nearbyOrders = new ArrayList<>();
        // REDIS_HASH_KEY(rider_locations)를 key로 가진 데이터 모두 가져오기 @redis
        Map<Object, Object> storedDeliveryRequests = redisTemplate.opsForHash().entries(REDIS_HASH_KEY);
        ObjectMapper objectMapper = new ObjectMapper();
        // redis에 저장되어있는 각 배달 건 확인
        for (Map.Entry<Object, Object> entry : storedDeliveryRequests.entrySet()) {
            String storedGeoHash = (String) entry.getValue(); // 배달 건의 geohash값 가져오기
            log.info(entry.toString());
            // 라이더의 5km 이내에 있는 geohash 범위에 배달 건의 geohash가 포함되는지 검증
            if (nearByHashes.contains(storedGeoHash)) {
                DeliveryRequest deliveryRequest = objectMapper.readValue(entry.getKey().toString(), DeliveryRequest.class);
                // 가게의 구(ex. 노원구) 꺼내기, storeAddress 예시: "서울시 노원구 동일로"
                String[] storeAddress = deliveryRequest.getStoreAddress().split(" ");
                String storeDistrict = storeAddress[1];
                // 가게가 내 활동 범위에 있는지, 배달 요청에 내 이동수단이 포함되는지 그리고 이미 라이더가 배정되었는지 검증
                if (deliveryRequest.isNotAssigned() && rider.getActivityArea().contains(storeDistrict)
                        && deliveryRequest.getTransportations().contains(rider.getTransportation())) {
                    // 가게 부담 배달비 계산 (이동수단과 가게 ~ 고객 거리 고려)
                    int costByDistance = (int) (rider.getTransportation().costPerKm * deliveryRequest.getDistanceFromStoreToCustomer());
                    int totalCost = deliveryRequest.getDeliveryPay() + costByDistance;
                    NearByOrderResponse response = new NearByOrderResponse(
                            deliveryRequest.getId(),
                            deliveryRequest.getOrderId(),
                            deliveryRequest.getStoreName(),
                            deliveryRequest.getStoreAddress(),
                            totalCost,
                            deliveryRequest.getDue().toLocalDateTime(),
                            deliveryRequest.getDistanceFromStoreToCustomer(),
                            deliveryRequest.getStoreLatitude(),
                            deliveryRequest.getStoreLongitude()
                    );
                    nearbyOrders.add(response);
                }
            }
        }
        return nearbyOrders;
    }




    @Override
    public void deleteById(UUID id) {
        DeliveryRequest deliveryRequest = deliveryRequestRedisRepository.findById(id)
                .orElseThrow(DeliveryRequestNotFoundException::new);
        deliveryRequestRedisRepository.deleteById(id);
    }

    @Override
    public void updateRiderAssigned(UUID id) {
        DeliveryRequest deliveryRequest = deliveryRequestRedisRepository.findById(id)
                .orElseThrow(DeliveryRequestNotFoundException::new);
        deliveryRequest.setAssigned(!deliveryRequest.isAssigned());
        deliveryRequestRedisRepository.save(deliveryRequest);
    }
}
