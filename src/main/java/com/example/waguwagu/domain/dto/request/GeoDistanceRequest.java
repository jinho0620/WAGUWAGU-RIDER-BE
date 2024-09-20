package com.example.waguwagu.domain.dto.request;

import jakarta.validation.constraints.NotNull;

public record GeoDistanceRequest(
        @NotNull Double startLat, @NotNull  Double startLong, @NotNull  Double endLat, @NotNull  Double endLong
) {
}
