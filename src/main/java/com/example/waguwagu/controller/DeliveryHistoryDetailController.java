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
@Tag(name = "Delivery History Detail")
public class DeliveryHistoryDetailController {
    private final DeliveryHistoryDetailService deliveryHistoryDetailService;

    @PostMapping("/delivery-history/{deliveryHistoryId}")
    @Operation(summary = "Save delivery history detail")
    public void saveDeliveryHistoryDetail(@PathVariable Long deliveryHistoryId, @RequestBody DeliveryHistoryDetailRequest req) {
        deliveryHistoryDetailService.saveDeliveryHistoryDetail(deliveryHistoryId, req);
    }

    @GetMapping("/delivery-history/{deliveryHistoryId}")
    @Operation(summary = "Retrieve delivery history details by delivery history ID")
    public List<DeliveryHistoryDetailResponse> getByDeliveryHistoryId(@PathVariable Long deliveryHistoryId) {
        return deliveryHistoryDetailService.getByDeliveryHistoryId(deliveryHistoryId);
    }

    @GetMapping("/summary/delivery-history/{deliveryHistoryId}")
    @Operation(summary = "Get the number of deliveries and total amount for a specific date by delivery history ID")
    public DeliveryHistorySummaryResponse getSummaryByDeliveryHistoryId(@PathVariable Long deliveryHistoryId) {
        return deliveryHistoryDetailService.getSummaryByDeliveryHistoryId(deliveryHistoryId);
    }
}
