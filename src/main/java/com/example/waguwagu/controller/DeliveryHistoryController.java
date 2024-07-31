package com.example.waguwagu.controller;

import com.example.waguwagu.domain.request.DurationForHistoryRequest;
import com.example.waguwagu.domain.request.DeliveryHistoryRequest;
import com.example.waguwagu.domain.response.DeliveryHistoryResponse;
import com.example.waguwagu.service.DeliveryHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/riders/{riderId}")
    public List<DeliveryHistoryResponse> getDeliveryHistories(@PathVariable Long riderId,
                                                              DurationForHistoryRequest dto) {
        return deliveryHistoryService.getDeliveryHistories(riderId, dto);
    }
}
