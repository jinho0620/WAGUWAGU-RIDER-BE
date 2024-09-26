package com.example.waguwagu.controller;

import com.example.waguwagu.domain.dto.response.DeliveryHistoryResponse;
import com.example.waguwagu.service.DeliveryHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/riders/delivery-histories")
@Tag(name = "배달 내역")
public class DeliveryHistoryController {
    private final DeliveryHistoryService deliveryHistoryService;

//    @PostMapping("/riders/{riderId}")
//    @ResponseStatus(HttpStatus.CREATED)
//    public void saveDeliveryHistory(@PathVariable Long riderId, @RequestBody DeliveryHistoryRequest req) {
//        deliveryHistoryService.saveDeliveryHistory(riderId, req);
//    }

//    @GetMapping("/riders/{riderId}/duration")
//    public List<Long> getDeliveryHistories(@PathVariable Long riderId, DurationForHistoryRequest dto) {
//        return deliveryHistoryService.getDeliveryHistories(riderId, dto);
//    }

    @PostMapping("/rider/{riderId}")
    @Operation(summary = "배달 내역 저장")
    public Long saveDeliveryHistory(@PathVariable Long riderId) {
        return deliveryHistoryService.saveDeliveryHistory(riderId);
    }

    @GetMapping("/rider/{riderId}")
    @Operation(summary = "배달 기사 ID로 배달 내역 가져오기")
    public List<DeliveryHistoryResponse> getDeliveryHistories(@PathVariable Long riderId) {
        return deliveryHistoryService.getDeliveryHistories(riderId);
    }
    @GetMapping("/today/rider/{riderId}")
    @Operation(summary = "배달 기사 ID로 배달 일자 및 요일 가져오기")
    public DeliveryHistoryResponse getTodayDeliveryHistory(@PathVariable Long riderId) {
        return deliveryHistoryService.getTodayDeliveryHistory(riderId);
    }
}
