package com.example.waguwagu.domain.request;

import com.example.waguwagu.domain.type.RiderTransportation;

import java.time.LocalDateTime;
import java.util.List;

public record DeliveryRequestFromKafka(
        Long orderId, // 주문 고유 번호
        String storeName, // 가게 이름
        String storeAddress, // 가게 주소
        int deliveryFee, // 배달료
        double distanceFromStoreToCustomer, // 가게~고객 거리
        double storeLongitude, // 가게 경도
        double storeLatitude, // 가게 위도
        LocalDateTime due // 배달 몇 시까지 해야 하는지?
) {

    public com.example.waguwagu.domain.entity.DeliveryRequest toEntity(List<RiderTransportation> transportations) {
        com.example.waguwagu.domain.entity.DeliveryRequest deliveryRequest = com.example.waguwagu.domain.entity.DeliveryRequest.builder()
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
