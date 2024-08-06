package com.example.waguwagu.domain.response;

import com.example.waguwagu.domain.entity.RiderLocation;

import java.util.UUID;

public record RiderLocationResponse(
        UUID orderId,
        double riderLatitude,
        double riderLongitude
) {
    public static RiderLocationResponse from(RiderLocation location) {
        RiderLocationResponse res = new RiderLocationResponse(
                location.getOrderId(),
                location.getRiderLatitude(),
                location.getRiderLongitude());
        return res;
    }
}
