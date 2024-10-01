package com.example.waguwagu.domain.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record NearByOrderResponse(
    UUID deliveryRequestId,
    UUID orderId,
    String storeName,
    String storeAddress,
    int deliveryPay,
    LocalDateTime due,
    double distanceFromStoreToCustomer,
    double storeLatitude,
    double storeLongitude
) {
}
