package com.example.waguwagu.service;

import com.example.waguwagu.domain.dto.request.GeoDistanceRequest;

public interface GeoDistanceService {
    double calculateDistance(GeoDistanceRequest req);
}
