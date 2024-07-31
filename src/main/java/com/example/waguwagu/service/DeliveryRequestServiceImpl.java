package com.example.waguwagu.service;

import com.example.waguwagu.domain.entity.DeliveryRequest;
import com.example.waguwagu.domain.entity.Rider;
import com.example.waguwagu.domain.request.DeliveryHistoryRequest;

import com.example.waguwagu.domain.request.RiderAssignRequest;
import com.example.waguwagu.domain.response.RiderAssignResponse;
import com.example.waguwagu.domain.type.RiderTransportation;
import com.example.waguwagu.global.exception.DeliveryRequestNotFoundException;
import com.example.waguwagu.global.exception.RiderNotActiveException;
import com.example.waguwagu.global.repository.DeliveryRequestRedisRepository;
import com.example.waguwagu.kafka.KafkaStatus;
import com.example.waguwagu.kafka.dto.KafkaDeliveryRequestDto;
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
import java.util.Arrays;
import java.util.List;
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
    private final DeliveryHistoryService deliveryHistoryService;

    @Override
    public List<RiderAssignResponse> assignRider(Long riderId, RiderAssignRequest req) {
        Rider rider = riderService.getById(riderId);
        if (!rider.isRiderIsActive()) throw new RiderNotActiveException(); // rider가 활성화 상태인지 확인
        List<com.example.waguwagu.domain.entity.DeliveryRequest> deliveryRequests = deliveryRequestRedisRepository.findAll();
        System.out.println(deliveryRequests);
        String key = "points";
        Metric metric = RedisGeoCommands.DistanceUnit.KILOMETERS;
        Point myLocation = new Point(req.longitude(), req.latitude());
        GeoOperations<String, String> geoOperations = redisTemplate.opsForGeo();
        geoOperations.add(key, myLocation, "me");
        List<RiderAssignResponse> list = new ArrayList<>();

        for (com.example.waguwagu.domain.entity.DeliveryRequest deliveryRequest : deliveryRequests) {
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
                if (distance.getValue() <= 5) {
                    System.out.println(deliveryRequest.getId());
                    RiderAssignResponse response = new RiderAssignResponse(
                            deliveryRequest.getId(),
                            deliveryRequest.getOrderId(),
                            deliveryRequest.getStoreName(),
                            deliveryRequest.getStoreAddress(),
                            deliveryRequest.getDeliveryPay(),
                            deliveryRequest.getDue(),
                            deliveryRequest.getDistanceFromStoreToCustomer(),
                            Math.floor(distance.getValue()*10)/10,
                            deliveryRequest.getStoreLatitude(),
                            deliveryRequest.getStoreLongitude()
                    );
                    list.add(response);
                };
            };
        };
//         요청 목록 return
        return list;
    }

//    @Override
//    public List<DeliveryRequest> getAll() {
//        return deliveryRequestRepository.findAll();
//    }

    @KafkaListener(topics = "order-topic", id = "delivery")
    public void save(KafkaStatus<KafkaDeliveryRequestDto> dto) {
        if (dto.status().equals("insert")) {
            System.out.println(dto);
            log.info("Order data successfully received for orderId: " + dto.data().orderId());
            List<RiderTransportation> transportations = new ArrayList<>();
            // 가게 ~ 고객 거리 < 1km 일 때, 모든 종류의 이동수단 가능
            if (dto.data().distanceFromStoreToCustomer() < 1) {
                transportations.addAll(Arrays.asList(
                        RiderTransportation.WALK,
                        RiderTransportation.BICYCLE,
                        RiderTransportation.MOTORBIKE,
                        RiderTransportation.CAR));
                // 1km <= 가게 ~ 고객 거리 < 2.5km 일 때, 도보 빼고 전부 가능
            } else if (dto.data().distanceFromStoreToCustomer() < 2.5) {
                transportations.addAll(Arrays.asList(
                        RiderTransportation.BICYCLE,
                        RiderTransportation.MOTORBIKE,
                        RiderTransportation.CAR));
                // 가게 ~ 고객 거리 >= 2.5km 일 때, 오토바이, 자동차만 가능
            } else if (dto.data().distanceFromStoreToCustomer() <= 5){
                transportations.addAll(Arrays.asList(
                        RiderTransportation.MOTORBIKE,
                        RiderTransportation.CAR));
            }
            com.example.waguwagu.domain.entity.DeliveryRequest deliveryRequest = dto.data().toEntity(transportations);
            deliveryRequestRedisRepository.save(deliveryRequest);
        }

    }


//    public void save(DeliveryRequestFromKafka dto) {
//            log.info("delivery request received~");
//            List<RiderTransportation> transportations = new ArrayList<>();
//            // 가게 ~ 고객 거리 < 1km 일 때, 모든 종류의 이동수단 가능
//            if (dto.distanceFromStoreToCustomer() < 1) {
//                transportations.addAll(Arrays.asList(
//                        RiderTransportation.WALK,
//                        RiderTransportation.BICYCLE,
//                        RiderTransportation.MOTORBIKE,
//                        RiderTransportation.CAR));
//                // 1km <= 가게 ~ 고객 거리 < 2.5km 일 때, 도보 빼고 전부 가능
//            } else if (dto.distanceFromStoreToCustomer() < 2.5) {
//                transportations.addAll(Arrays.asList(
//                        RiderTransportation.BICYCLE,
//                        RiderTransportation.MOTORBIKE,
//                        RiderTransportation.CAR));
//                // 가게 ~ 고객 거리 >= 2.5km 일 때, 오토바이, 자동차만 가능
//            } else if (dto.distanceFromStoreToCustomer() <= 5){
//                transportations.addAll(Arrays.asList(
//                        RiderTransportation.MOTORBIKE,
//                        RiderTransportation.CAR));
//            }
//            com.example.waguwagu.domain.entity.DeliveryRequest deliveryRequest = dto.toEntity(transportations);
//            deliveryRequestRedisRepository.save(deliveryRequest);
//    }

    @Override
    public void deleteFromRedisAndSaveToDatabase(UUID id, Long riderId) {
        System.out.println(riderId);
        System.out.println("method 입장 완료");
        DeliveryRequest deliveryRequest = deliveryRequestRedisRepository.findById(id)
                .orElseThrow(DeliveryRequestNotFoundException::new);
        System.out.println(deliveryRequest);
        DeliveryHistoryRequest deliveryHistoryRequest = new DeliveryHistoryRequest(
                deliveryRequest.getDeliveryPay(),
                deliveryRequest.getStoreName(),
                deliveryRequest.getOrderId()
        );
        deliveryHistoryService.saveDeliveryHistory(riderId, deliveryHistoryRequest);
        System.out.println("deliveryHistoryRequest 생성 완료");
        deleteById(id);
        System.out.println("주문 건 레디스에서 삭제 완료");
    }

    @Override
    public void deleteById(UUID id) {
        DeliveryRequest deliveryRequest = deliveryRequestRedisRepository.findById(id)
                .orElseThrow(DeliveryRequestNotFoundException::new);
        deliveryRequestRedisRepository.deleteById(id);
    }
}
