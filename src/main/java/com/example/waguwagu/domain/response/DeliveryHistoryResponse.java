package com.example.waguwagu.domain.response;

import com.example.waguwagu.domain.entity.DeliveryHistory;
import com.example.waguwagu.domain.entity.DeliveryHistoryDetail;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record DeliveryHistoryResponse (
        Long deliveryHistoryId,
        LocalDate deliveryHistoryCreatedAt,
        DayOfWeek deliveryDay

) {
    public static DeliveryHistoryResponse from(DeliveryHistory history) {
        DeliveryHistoryResponse deliveryHistory = new DeliveryHistoryResponse(
                history.getDeliveryHistoryId(),
                history.getDeliveryHistoryCreatedAt(),
                history.getDeliveryHistoryCreatedAt().getDayOfWeek()
        );
        return deliveryHistory;
    }
}
