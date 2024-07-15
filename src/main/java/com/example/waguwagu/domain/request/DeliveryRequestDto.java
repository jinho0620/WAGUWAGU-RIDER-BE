package com.example.waguwagu.domain.request;

import com.example.waguwagu.domain.entity.DeliveryRequest;
import com.example.waguwagu.domain.type.RiderTransportation;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public record DeliveryRequestDto(
//        Long id,
        String storeName,
        String storeAddress,
        int deliveryPay,
        int distanceFromStoreToCustomer,
        double storeLatitude,
        double storeLongitude,
        LocalDateTime due

) {
    public DeliveryRequest toEntity(List<RiderTransportation> transportations) {
        DeliveryRequest deliveryRequest = DeliveryRequest.builder()
                .storeName(storeName)
                .storeAddress(storeAddress)
                .deliveryPay(deliveryPay)
                .distanceFromStoreToCustomer(distanceFromStoreToCustomer)
                .storeLatitude(storeLatitude)
                .storeLongitude(storeLongitude)
                .transportations(transportations)
                .due(due)
                .build();
        return deliveryRequest;
    }
}
