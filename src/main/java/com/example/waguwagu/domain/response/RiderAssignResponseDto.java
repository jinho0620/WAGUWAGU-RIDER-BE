package com.example.waguwagu.domain.response;

import java.time.LocalDateTime;

public record RiderAssignResponseDto(
        String storeName,
        String storeAddress,
        int deliveryPay,
        LocalDateTime due,
        int distanceFromStoreToCustomer,
        double distanceFromStoreToRider

) {
}
