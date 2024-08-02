package com.example.waguwagu.domain.response;

import java.time.LocalDateTime;

public record DeliveryHistorySummaryResponse(
        int deliveryCount,
        int deliveryTotalIncome
) {
}
