package com.example.waguwagu.domain.response;

import com.example.waguwagu.domain.entity.DeliveryHistoryDetail;

import java.time.LocalDateTime;


public record DeliveryHistoryDetailResponse(
        Long deliveryHistoryDetailId,
        int deliveryIncome,
        LocalDateTime deliveryHistoryDetailCreatedAt,
        String storeName
) {
    public static DeliveryHistoryDetailResponse from(DeliveryHistoryDetail detail) {
        DeliveryHistoryDetailResponse res = new DeliveryHistoryDetailResponse(
                detail.getDeliveryHistoryDetailId(),
                detail.getDeliveryIncome(),
                detail.getDeliveryHistoryDetailCreatedAt(),
                detail.getStoreName()
        );
        return res;
    }
}
