package com.example.waguwagu.kafka.dto;

import com.example.waguwagu.domain.entity.DeliveryRequest;
import com.example.waguwagu.domain.type.RiderTransportation;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

public record KafkaDeliveryRequestDto(
        Long orderId,
        String storeName, // 가게 이름
        String storeAddress, // 가게 주소
        int deliveryFee, // 배달료
        double distanceFromStoreToCustomer, // 가게~고객 거리
        double storeLongitude, // 가게 경도
        double storeLatitude, // 가게 위도
        LocalDateTime due // 배달 몇 시까지 해야 하는지?
) {
    public DeliveryRequest toEntity(List<RiderTransportation> transportations) {
        DeliveryRequest deliveryRequest = DeliveryRequest.builder()
                .orderId(orderId)
                .storeName(storeName)
                .storeAddress(storeAddress)
                .deliveryPay(deliveryFee)
                .distanceFromStoreToCustomer(distanceFromStoreToCustomer)
                .storeLongitude(storeLongitude)
                .storeLatitude(storeLatitude)
                .transportations(transportations)
                .deliveryPay((int) Math.round(deliveryFee * 0.9))
                .due(due)
                .build();
        return deliveryRequest;
    }
}
