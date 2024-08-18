package com.example.waguwagu.controller;

import com.example.waguwagu.domain.dto.request.DeliveryHistoryDetailRequest;
import com.example.waguwagu.domain.dto.response.DeliveryHistoryDetailResponse;
import com.example.waguwagu.domain.dto.response.DeliveryHistorySummaryResponse;
import com.example.waguwagu.service.DeliveryHistoryDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/riders/delivery-history-details")
public class DeliveryHistoryDetailController {
    private final DeliveryHistoryDetailService deliveryHistoryDetailService;

    @PostMapping("/delivery-history/{deliveryHistoryId}")
    public void saveDeliveryHistoryDetail(@PathVariable Long deliveryHistoryId, @RequestBody DeliveryHistoryDetailRequest req) {
        deliveryHistoryDetailService.saveDeliveryHistoryDetail(deliveryHistoryId, req);
    }

    @GetMapping("/delivery-history/{deliveryHistoryId}")
    public List<DeliveryHistoryDetailResponse> getByDeliveryHistoryId(@PathVariable Long deliveryHistoryId) {
        return deliveryHistoryDetailService.getByDeliveryHistoryId(deliveryHistoryId);
    }
    @GetMapping("summary/delivery-history/{deliveryHistoryId}")
    public DeliveryHistorySummaryResponse getSummaryByDeliveryHistoryId(@PathVariable Long deliveryHistoryId) {
        return deliveryHistoryDetailService.getSummaryByDeliveryHistoryId(deliveryHistoryId);
    }

}
