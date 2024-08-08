package com.example.waguwagu.controller;

import com.example.waguwagu.domain.dto.request.RiderLocationRequest;
import com.example.waguwagu.domain.dto.response.RiderLocationResponse;
import com.example.waguwagu.service.RiderLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rider-locations")
public class RiderLocationController {
    private final RiderLocationService riderLocationService;

    @PostMapping
    public void saveRiderLocation(@RequestBody RiderLocationRequest req) {
        riderLocationService.saveRiderLocation(req);
    }

    @GetMapping("/{orderId}")
    public RiderLocationResponse getByOrderId(@PathVariable UUID orderId) {
        return riderLocationService.getByOrderId(orderId);
    }

    @DeleteMapping("/{orderId}")
    public void deleteByOrderId(@PathVariable UUID orderId) {
        riderLocationService.deleteByOrderId(orderId);
    }
}
