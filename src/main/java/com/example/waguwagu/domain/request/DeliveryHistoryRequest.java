package com.example.waguwagu.domain.request;

import com.example.waguwagu.domain.entity.DeliveryHistory;
import com.example.waguwagu.domain.entity.Rider;

import java.time.LocalDateTime;
import java.util.UUID;

public record DeliveryHistoryRequest(
        int deliveryIncome,
        String storeName,
        UUID orderId
) {
    public DeliveryHistory toEntity(Rider rider) {
        DeliveryHistory history = DeliveryHistory.builder()
                .rider(rider)
                .deliveryIncome(deliveryIncome)
                .storeName(storeName)
                .orderId(orderId)
                .build();
        return history;
    }
}
