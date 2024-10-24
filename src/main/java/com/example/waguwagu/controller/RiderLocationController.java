package com.example.waguwagu.controller;

import com.example.waguwagu.domain.dto.request.RiderLocationRequest;
import com.example.waguwagu.domain.dto.response.RiderLocationResponse;
import com.example.waguwagu.service.RiderLocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/riders/rider-locations")
@Tag(name = "Delivery Rider Location")
public class RiderLocationController {
    private final RiderLocationService riderLocationService;

    @PostMapping
    @Operation(summary = "Save delivery rider location")
    public void saveRiderLocation(@RequestBody RiderLocationRequest req) {
        riderLocationService.saveRiderLocation(req);
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Retrieve delivery rider location and transportation method by order ID")
    public RiderLocationResponse getByOrderId(@PathVariable UUID orderId) {
        return riderLocationService.getByOrderId(orderId);
    }

    @DeleteMapping("/{orderId}")
    @Operation(summary = "Delete delivery rider location by order ID")
    public void deleteByOrderId(@PathVariable UUID orderId) {
        riderLocationService.deleteByOrderId(orderId);
    }
}
