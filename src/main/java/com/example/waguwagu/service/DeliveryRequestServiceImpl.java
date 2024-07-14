package com.example.waguwagu.service;

import com.example.waguwagu.domain.entity.DeliveryRequest;
import com.example.waguwagu.domain.entity.Rider;
import com.example.waguwagu.domain.request.RiderAssignRequestDto;
import com.example.waguwagu.domain.response.RiderAssignResponseDto;
import com.example.waguwagu.global.repository.DeliveryRequestRepository;
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
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor

/* 요청이 올 때마다 DB를 뒤지는 것은 비효율적이다. 요청이 들어올 때마다, 라이더들을 추려내서 쏴주는 것이 가장 효율적이다. -> socket 이용
1. 현재 활동 범위 안에 있다면 gps로 위, 경도를 쏜다. @front
2. DB에서 목록을 가져온 후 이동 수단이 라이더의 것과 맞는 것만 추려냄 -> 여기까지 진행하면 내가 배달할 권리가 있는 것들을 추려냄 @back
3. 걸러낸 각 요청들에서 라이더~가게 사이의 거리를 계산
4. 가게 이름/주소, 배달료, 배달 목표 시간, 가게~라이더 사이 거리, 가게~고객 거리를 라이더한테 줌
*/
public class DeliveryRequestServiceImpl implements DeliveryRequestService {

    private final DeliveryRequestRepository deliveryRequestRepository;

    @Override
    public List<RiderAssignResponseDto> assignRider(RiderAssignRequestDto req) {
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory();
        connectionFactory.afterPropertiesSet();
        RedisTemplate<String, String> geoTemplate = new RedisTemplate<>();
        geoTemplate.setConnectionFactory(connectionFactory);
        geoTemplate.setKeySerializer(new StringRedisSerializer());
        geoTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(new ObjectMapper(), DeliveryRequest.class));
        geoTemplate.afterPropertiesSet();

        List<DeliveryRequest> deliveryRequests = deliveryRequestRepository.findAll();
        System.out.println(deliveryRequests);
        String key = "points";
        Metric metric = RedisGeoCommands.DistanceUnit.KILOMETERS;
        Point myLocation = new Point(req.latitude(), req.longitude());
        GeoOperations<String, String> geoOperations = geoTemplate.opsForGeo();
        geoOperations.add(key, myLocation, "me");
        List<RiderAssignResponseDto> list = new ArrayList<>();
        for (DeliveryRequest deliveryRequest : deliveryRequests) {

            // 내 이동 수단을 포함하고 있는지 확인
            if(deliveryRequest.getTransportation().contains("MOTORBIKE")) {
                // 맞다면 요청 목록에 추가
                geoOperations.add(
                        key,
                        new Point(deliveryRequest.getStoreLatitude(), deliveryRequest.getStoreLongitude()),
                        "store");
                Distance distance = geoOperations.distance(key, "me", "store", metric);
                System.out.println(distance);
                System.out.println(distance.getValue());
                RiderAssignResponseDto response = new RiderAssignResponseDto(
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


    @Override
    public void save(DeliveryRequest req) {
        deliveryRequestRepository.save(req);
        log.info("Success");
    }

    @Override
    public List<DeliveryRequest> getAll() {
        return deliveryRequestRepository.findAll();
    }
}
