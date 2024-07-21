package com.example.waguwagu.domain.response;

import java.time.LocalDateTime;

public record RiderAssignResponseDto(
        Long orderId,
        String storeName,
        String storeAddress,
        int deliveryPay,
        LocalDateTime due,
        double distanceFromStoreToCustomer,
        double distanceFromStoreToRider

) {
}
