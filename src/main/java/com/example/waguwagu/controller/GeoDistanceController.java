package com.example.waguwagu.controller;

import com.example.waguwagu.domain.dto.request.GeoDistanceRequest;
import com.example.waguwagu.service.GeoDistanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/geo")
public class GeoDistanceController {
    private final GeoDistanceService geoDistanceService;

    @PostMapping
    public double calculateDistance(@RequestBody GeoDistanceRequest req) {
        return geoDistanceService.calculateDistance(req);
    }
}
