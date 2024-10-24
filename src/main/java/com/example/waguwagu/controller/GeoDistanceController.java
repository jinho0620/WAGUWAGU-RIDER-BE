package com.example.waguwagu.controller;

import com.example.waguwagu.domain.dto.request.GeoDistanceRequest;
import com.example.waguwagu.service.GeoDistanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/riders/geo")
@Tag(name = "Straight-Line Distance Calculation")
public class GeoDistanceController {
    private final GeoDistanceService geoDistanceService;

    @PostMapping
    @Operation(summary = "Calculate straight-line distance between two points based on latitude and longitude")
    public double calculateDistance(@RequestBody GeoDistanceRequest req) {
        return geoDistanceService.calculateDistance(req);
    }
}
