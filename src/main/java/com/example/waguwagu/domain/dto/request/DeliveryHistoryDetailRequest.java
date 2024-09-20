package com.example.waguwagu.domain.dto.request;

import com.example.waguwagu.domain.entity.DeliveryHistory;
import com.example.waguwagu.domain.entity.DeliveryHistoryDetail;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

public record DeliveryHistoryDetailRequest(
    int deliveryIncome,
    String storeName,
    UUID orderId
) {
    public DeliveryHistoryDetail toEntity(DeliveryHistory history) {
        DeliveryHistoryDetail detail = DeliveryHistoryDetail.builder()
                .deliveryIncome(deliveryIncome)
                .storeName(storeName)
                .orderId(orderId)
                .deliveryHistory(history)
                .build();
        return detail;
    }
}
