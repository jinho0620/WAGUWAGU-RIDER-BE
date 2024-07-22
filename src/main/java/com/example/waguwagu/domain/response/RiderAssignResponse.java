package com.example.waguwagu.domain.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record RiderAssignResponse(
        UUID deliveryRequestId,
        Long orderId,
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
