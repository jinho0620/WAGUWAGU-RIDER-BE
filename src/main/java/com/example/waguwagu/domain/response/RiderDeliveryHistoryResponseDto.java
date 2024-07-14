package com.example.waguwagu.domain.response;

import com.example.waguwagu.domain.entity.RiderDeliveryHistory;

import java.time.LocalDateTime;

public record RiderDeliveryHistoryResponseDto(
        Long riderDeliveryHistoryId,
        int riderIncome,
        LocalDateTime riderIncomeCreatedAt,
        String riderStoreName
) {
    public static RiderDeliveryHistoryResponseDto from(RiderDeliveryHistory history) {
        RiderDeliveryHistoryResponseDto dto = new RiderDeliveryHistoryResponseDto(
                history.getRiderDeliveryHistoryId(),
                history.getRiderIncome(),
                history.getRiderIncomeCreatedAt(),
                history.getRiderStoreName());
        return dto;
    }
}
