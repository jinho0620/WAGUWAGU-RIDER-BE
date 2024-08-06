package com.example.waguwagu.domain.response;

import java.time.LocalDateTime;

public record DeliveryHistorySummaryResponse(
        int deliveryCount,
        int deliveryTotalIncome
) {
    @Override
    public String toString() {
        return "DeliveryHistorySummaryResponse{" +
                "deliveryCount=" + deliveryCount +
                ", deliveryTotalIncome=" + deliveryTotalIncome +
                '}';
    }
}
