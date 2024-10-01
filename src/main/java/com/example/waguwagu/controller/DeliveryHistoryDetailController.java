package com.example.waguwagu.controller;

import com.example.waguwagu.domain.dto.request.DeliveryHistoryDetailRequest;
import com.example.waguwagu.domain.dto.response.DeliveryHistoryDetailResponse;
import com.example.waguwagu.domain.dto.response.DeliveryHistorySummaryResponse;
import com.example.waguwagu.service.DeliveryHistoryDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/riders/delivery-history-details")
@Tag(name = "배달 내역 상세")
public class DeliveryHistoryDetailController {
    private final DeliveryHistoryDetailService deliveryHistoryDetailService;

    @PostMapping("/delivery-history/{deliveryHistoryId}")
    @Operation(summary = "배달 내역 상세 저장")
    public void saveDeliveryHistoryDetail(@PathVariable Long deliveryHistoryId, @RequestBody DeliveryHistoryDetailRequest req) {
        deliveryHistoryDetailService.saveDeliveryHistoryDetail(deliveryHistoryId, req);
    }

    @GetMapping("/delivery-history/{deliveryHistoryId}")
    @Operation(summary = "배달 내역 ID로 배달 내역 상세 가져오기")
    public List<DeliveryHistoryDetailResponse> getByDeliveryHistoryId(@PathVariable Long deliveryHistoryId) {
        return deliveryHistoryDetailService.getByDeliveryHistoryId(deliveryHistoryId);
    }
    @GetMapping("/summary/delivery-history/{deliveryHistoryId}")
    @Operation(summary = "배달 내역 ID로 특정 날짜의 배달 횟수 및 배달 합산액 가져오기")
    public DeliveryHistorySummaryResponse getSummaryByDeliveryHistoryId(@PathVariable Long deliveryHistoryId) {
        return deliveryHistoryDetailService.getSummaryByDeliveryHistoryId(deliveryHistoryId);
    }

}
