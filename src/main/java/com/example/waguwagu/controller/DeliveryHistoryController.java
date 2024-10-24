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
@Tag(name = "Delivery History")
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
    @Operation(summary = "Store delivery history")
    public Long saveDeliveryHistory(@PathVariable Long riderId) {
        return deliveryHistoryService.saveDeliveryHistory(riderId);
    }

    @GetMapping("/rider/{riderId}")
    @Operation(summary = "Retrieve delivery history by id")
    public List<DeliveryHistoryResponse> getDeliveryHistories(@PathVariable Long riderId) {
        return deliveryHistoryService.getDeliveryHistories(riderId);
    }
    @GetMapping("/today/rider/{riderId}")
    @Operation(summary = "Retrieve delivery date and day of the week by delivery rider ID")
    public DeliveryHistoryResponse getTodayDeliveryHistory(@PathVariable Long riderId) {
        return deliveryHistoryService.getTodayDeliveryHistory(riderId);
    }
}
