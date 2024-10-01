package com.example.waguwagu.domain.dto.response;

import com.example.waguwagu.domain.entity.DeliveryHistory;

import java.time.DayOfWeek;
import java.time.LocalDate;

public record DeliveryHistoryResponse (
        Long deliveryHistoryId,
        LocalDate deliveryHistoryCreatedAt,
        String deliveryDay

) {
    public static DeliveryHistoryResponse from(DeliveryHistory history) {
        DeliveryHistoryResponse deliveryHistory = new DeliveryHistoryResponse(
                history.getId(),
                history.getCreatedAt(),
                getDayOfWeekInKorean(history.getCreatedAt().getDayOfWeek())
        );
        return deliveryHistory;
    }

    public static String getDayOfWeekInKorean(DayOfWeek deliveryDay) {
        switch (deliveryDay) {
            case MONDAY -> {
                return "월요일";
            }
            case TUESDAY -> {
                return "화요일";
            }
            case WEDNESDAY -> { 
                return "수요일";
            }
            case THURSDAY -> {
                return "목요일";
            }
            case FRIDAY -> {
                return "금요일";
            }
            case SATURDAY -> {
                return "토요일";
            }
            case SUNDAY -> {
                return "일요일";
            }
        }
        return null;
    }
}
