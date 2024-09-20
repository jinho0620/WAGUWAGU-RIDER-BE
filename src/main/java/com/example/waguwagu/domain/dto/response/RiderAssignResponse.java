package com.example.waguwagu.domain.dto.response;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

public record RiderAssignResponse(
        UUID deliveryRequestId,
        UUID orderId,
        String storeName,
        String storeAddress,
        int deliveryPay,
        LocalDateTime due,
        double distanceFromStoreToCustomer,
        double distanceFromStoreToRider,
        double storeLatitude,
        double storeLongitude

) {
}
