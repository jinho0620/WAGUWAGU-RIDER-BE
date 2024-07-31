package com.example.waguwagu.domain.response;

import com.example.waguwagu.domain.entity.DeliveryHistory;

import java.time.LocalDateTime;
import java.util.UUID;

public record DeliveryHistoryResponse(
        Long deliveryHistoryId,
        int deliveryIncome,
        LocalDateTime deliveryIncomeCreatedAt,
        UUID orderId,
        String storeName
) {
    public static DeliveryHistoryResponse from(DeliveryHistory history) {
        DeliveryHistoryResponse dto = new DeliveryHistoryResponse(
                history.getDeliveryHistoryId(),
                history.getDeliveryIncome(),
                history.getDeliveryIncomeCreatedAt(),
                history.getOrderId(),
                history.getStoreName());
        return dto;
    }
}
