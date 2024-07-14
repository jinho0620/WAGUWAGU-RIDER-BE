package com.example.waguwagu.domain.request;

import com.example.waguwagu.domain.entity.Rider;
import com.example.waguwagu.domain.entity.RiderDeliveryHistory;

import java.time.LocalDateTime;
import java.util.UUID;

public record RiderDeliveryHistoryRequestDto(
        int riderIncome,
        String riderStoreName
) {
    public RiderDeliveryHistory toEntity(Rider rider) {
        RiderDeliveryHistory history = RiderDeliveryHistory.builder()
                .rider(rider)
                .riderIncome(riderIncome)
                .riderStoreName(riderStoreName)
                .riderIncomeCreatedAt(LocalDateTime.now())
                .build();
        return history;
    }
}
