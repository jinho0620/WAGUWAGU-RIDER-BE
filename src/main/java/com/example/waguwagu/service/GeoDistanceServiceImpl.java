package com.example.waguwagu.service;

import com.example.waguwagu.domain.dto.request.GeoDistanceRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeoDistanceServiceImpl implements GeoDistanceService {
    private final int EARTH_RADIUS = 6371;
    double haversine(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }

    @Override
    public double calculateDistance(GeoDistanceRequest req) {
        double dLat = Math.toRadians((req.endLat() - req.startLat()));
        double dLong = Math.toRadians((req.endLong() - req.startLong()));

        double startLat = Math.toRadians(req.startLat());
        double endLat = Math.toRadians(req.endLat());

        double a = haversine(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversine(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (double) (Math.floor(EARTH_RADIUS * c * 10) / 10);
    }
}
