package com.example.waguwagu.controller;

import com.example.waguwagu.domain.dto.response.DeliveryHistoryResponse;
import com.example.waguwagu.service.DeliveryHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/delivery-histories")
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
    public Long saveDeliveryHistory(@PathVariable Long riderId) {
        return deliveryHistoryService.saveDeliveryHistory(riderId);
    }

    @GetMapping("/rider/{riderId}")
    public List<DeliveryHistoryResponse> getDeliveryHistories(@PathVariable Long riderId) {
        return deliveryHistoryService.getDeliveryHistories(riderId);
    }
}
