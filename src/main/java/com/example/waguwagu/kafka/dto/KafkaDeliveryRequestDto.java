package com.example.waguwagu.kafka.dto;

import com.example.waguwagu.domain.entity.DeliveryRequest;
import com.example.waguwagu.domain.type.Transportation;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public record KafkaDeliveryRequestDto(
        UUID orderId, // 주문 고유 번호
        String storeName, // 가게 이름
        String storeAddress, // 가게 주소
        int deliveryFee, // 배달료
        double distanceFromStoreToCustomer, // 가게~고객 거리
        double storeLongitude, // 가게 경도
        double storeLatitude, // 가게 위도
        Timestamp due // 배달 몇 시까지 해야 하는지?
) {

    public DeliveryRequest toEntity(List<Transportation> transportations) {
        DeliveryRequest deliveryRequest = DeliveryRequest.builder()
                .id(orderId)
                .orderId(orderId)
                .storeName(storeName)
                .storeAddress(storeAddress)
                .distanceFromStoreToCustomer(distanceFromStoreToCustomer)
                .storeLongitude(storeLongitude)
                .storeLatitude(storeLatitude)
                .transportations(transportations)
                .deliveryPay((int) Math.round(deliveryFee * 0.9))
                .due(due)
                .build();
        return deliveryRequest;
    }

    @Override
    public String toString() {
        return "KafkaDeliveryRequestDto{" +
                "orderId=" + orderId +
                ", storeName='" + storeName + '\'' +
                ", storeAddress='" + storeAddress + '\'' +
                ", deliveryFee=" + deliveryFee +
                ", distanceFromStoreToCustomer=" + distanceFromStoreToCustomer +
                ", storeLongitude=" + storeLongitude +
                ", storeLatitude=" + storeLatitude +
                ", due=" + due +
                '}';
    }
}
