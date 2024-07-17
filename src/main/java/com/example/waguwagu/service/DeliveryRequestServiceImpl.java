package com.example.waguwagu.service;

import com.example.waguwagu.domain.entity.DeliveryRequest;
import com.example.waguwagu.domain.entity.Rider;
import com.example.waguwagu.domain.request.RiderAssignRequestDto;
import com.example.waguwagu.domain.response.RiderAssignResponseDto;
import com.example.waguwagu.domain.type.RiderTransportation;
import com.example.waguwagu.global.repository.DeliveryRequestRepository;
import com.example.waguwagu.kafka.dto.KafkaDeliveryRequestDto;
import com.example.waguwagu.kafka.KafkaStatus;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metric;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private final DeliveryRequestRepository deliveryRequestRepository;
    private final RiderService riderService;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public List<RiderAssignResponseDto> assignRider(Long riderId, RiderAssignRequestDto req) {

        List<DeliveryRequest> deliveryRequests = deliveryRequestRepository.findAll();
        System.out.println(deliveryRequests);
        String key = "points";
        Metric metric = RedisGeoCommands.DistanceUnit.KILOMETERS;
        Point myLocation = new Point(req.longitude(), req.latitude());
        GeoOperations<String, String> geoOperations = redisTemplate.opsForGeo();
        geoOperations.add(key, myLocation, "me");
        List<RiderAssignResponseDto> list = new ArrayList<>();
        Rider rider = riderService.getById(riderId);

        for (DeliveryRequest deliveryRequest : deliveryRequests) {

            // 가게의 구(ex. 노원구) 꺼내기
            String[] storeAddress = deliveryRequest.getStoreAddress().split(" ");
            String storeDistrict = storeAddress[1];

            // 가게가 내 활동 범위에 있는지, 배달 요청에 내 이동수단이 포함되는지 확인
            if(rider.getRiderActivityArea().contains(storeDistrict)
                    && deliveryRequest.getTransportations().contains(rider.getRiderTransportation())) {
                // 맞다면 요청 목록에 추가
                geoOperations.add(
                        key,
                        new Point(deliveryRequest.getStoreLongitude(), deliveryRequest.getStoreLatitude()),
                        "store");
                Distance distance = geoOperations.distance(key, "me", "store", metric);
                System.out.println(distance);
                System.out.println(distance.getValue());
                RiderAssignResponseDto response = new RiderAssignResponseDto(
                        deliveryRequest.getOrderId(),
                        deliveryRequest.getStoreName(),
                        deliveryRequest.getStoreAddress(),
                        deliveryRequest.getDeliveryPay(),
                        deliveryRequest.getDue(),
                        deliveryRequest.getDistanceFromStoreToCustomer(),
                        distance.getValue()
                );
                list.add(response);
            }
        }
//         요청 목록 return
        return list;
    }

//    @Override
//    public List<DeliveryRequest> getAll() {
//        return deliveryRequestRepository.findAll();
//    }

    @KafkaListener(topics = "delivery-request-topic", id = "delivery")
    public void save(KafkaStatus<KafkaDeliveryRequestDto> kafkaStatus) {
        log.info("delivery request received~");
        if (kafkaStatus.status().equals("assign")) {
        List<RiderTransportation> transportations = new ArrayList<>();
        // 가게 ~ 고객 거리 < 1km 일 때, 모든 종류의 이동수단 가능
        if (kafkaStatus.data().distanceFromStoreToCustomer() < 1) {
            transportations.addAll(Arrays.asList(
                    RiderTransportation.WALK,
                    RiderTransportation.BICYCLE,
                    RiderTransportation.MOTORBIKE,
                    RiderTransportation.CAR));
        // 1km <= 가게 ~ 고객 거리 < 2.5km 일 때, 도보 빼고 전부 가능
        } else if (kafkaStatus.data().distanceFromStoreToCustomer() < 2.5) {
            transportations.addAll(Arrays.asList(
                    RiderTransportation.BICYCLE,
                    RiderTransportation.MOTORBIKE,
                    RiderTransportation.CAR));
        // 가게 ~ 고객 거리 >= 2.5km 일 때, 오토바이, 자동차만 가능
        } else {
            transportations.addAll(Arrays.asList(
                    RiderTransportation.MOTORBIKE,
                    RiderTransportation.CAR));
        }
            DeliveryRequest deliveryRequest = kafkaStatus.data().toEntity(transportations);
            deliveryRequestRepository.save(deliveryRequest);
        }
    }
}
