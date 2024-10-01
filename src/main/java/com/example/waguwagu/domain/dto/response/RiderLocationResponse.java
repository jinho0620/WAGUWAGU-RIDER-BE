package com.example.waguwagu.domain.dto.response;

import com.example.waguwagu.domain.entity.RiderLocation;
import com.example.waguwagu.domain.type.Transportation;

public record RiderLocationResponse(
        double riderLatitude,
        double riderLongitude,
        Transportation transportation
) {
    public static RiderLocationResponse from(RiderLocation location, Transportation transportation) {
        RiderLocationResponse res = new RiderLocationResponse(
                location.getRiderLatitude(),
                location.getRiderLongitude(),
                transportation);
        return res;
    }
}
