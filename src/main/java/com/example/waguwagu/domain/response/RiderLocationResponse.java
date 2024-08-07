package com.example.waguwagu.domain.response;

import com.example.waguwagu.domain.entity.RiderLocation;
import com.example.waguwagu.domain.type.RiderTransportation;

import java.util.UUID;

public record RiderLocationResponse(
        double riderLatitude,
        double riderLongitude,
        RiderTransportation riderTransportation
) {
    public static RiderLocationResponse from(RiderLocation location, RiderTransportation riderTransportation) {
        RiderLocationResponse res = new RiderLocationResponse(
                location.getRiderLatitude(),
                location.getRiderLongitude(),
                riderTransportation);
        return res;
    }
}
