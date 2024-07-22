package com.example.waguwagu.domain.response;

import com.example.waguwagu.domain.entity.RiderDeliveryHistory;

import java.time.LocalDateTime;

public record RiderDeliveryHistoryResponse(
        Long riderDeliveryHistoryId,
        int riderIncome,
        LocalDateTime riderIncomeCreatedAt,
        String riderStoreName
) {
    public static RiderDeliveryHistoryResponse from(RiderDeliveryHistory history) {
        RiderDeliveryHistoryResponse dto = new RiderDeliveryHistoryResponse(
                history.getRiderDeliveryHistoryId(),
                history.getRiderIncome(),
                history.getRiderIncomeCreatedAt(),
                history.getRiderStoreName());
        return dto;
    }
}
