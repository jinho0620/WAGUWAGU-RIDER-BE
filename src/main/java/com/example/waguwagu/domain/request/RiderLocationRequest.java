package com.example.waguwagu.domain.request;

import com.example.waguwagu.domain.entity.RiderLocation;

import java.util.UUID;

public record RiderLocationRequest(
        UUID orderId,
        double riderLatitude,
        double riderLongitude,
        Long riderId
) {
    public RiderLocation toEntity() {
        RiderLocation location = RiderLocation.builder()
                .id(orderId)
                .orderId(orderId)
                .riderLatitude(riderLatitude)
                .riderLongitude(riderLongitude)
                .riderId(riderId)
                .build();
        return location;
    }

    @Override
    public String toString() {
        return "RiderLocationRequest{" +
                "orderId=" + orderId +
                ", riderLatitude=" + riderLatitude +
                ", riderLongitude=" + riderLongitude +
                ", riderId=" + riderId +
                '}';
    }
}
