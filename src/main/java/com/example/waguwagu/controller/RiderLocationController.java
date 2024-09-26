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
@Tag(name = "배달 기사 위치")
public class RiderLocationController {
    private final RiderLocationService riderLocationService;

    @PostMapping
    @Operation(summary = "배달 기사 위치 저장")
    public void saveRiderLocation(@RequestBody RiderLocationRequest req) {
        riderLocationService.saveRiderLocation(req);
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "주문 ID로 배달 기사 위치 및 이동 수단 가져오기")
    public RiderLocationResponse getByOrderId(@PathVariable UUID orderId) {
        return riderLocationService.getByOrderId(orderId);
    }

    @DeleteMapping("/{orderId}")
    @Operation(summary = "주문 ID로 배달 기사 위치 삭제")
    public void deleteByOrderId(@PathVariable UUID orderId) {
        riderLocationService.deleteByOrderId(orderId);
    }
}
