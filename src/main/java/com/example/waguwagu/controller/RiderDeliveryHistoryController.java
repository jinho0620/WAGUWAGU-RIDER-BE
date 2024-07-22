package com.example.waguwagu.controller;

import com.example.waguwagu.domain.request.DurationForHistoryRequest;
import com.example.waguwagu.domain.request.RiderDeliveryHistoryRequest;
import com.example.waguwagu.domain.response.RiderDeliveryHistoryResponse;
import com.example.waguwagu.service.RiderDeliveryHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/delivery-histories")
public class RiderDeliveryHistoryController {
    private final RiderDeliveryHistoryService riderDeliveryHistoryService;

    @PostMapping("/riders/{riderId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveDeliveryHistory(@PathVariable Long riderId, @RequestBody RiderDeliveryHistoryRequest req) {
        riderDeliveryHistoryService.saveDeliveryHistory(riderId, req);
    }

    @GetMapping("/riders/{riderId}")
    public List<RiderDeliveryHistoryResponse> getDeliveryHistories(@PathVariable Long riderId,
                                                                   DurationForHistoryRequest dto) {
        return riderDeliveryHistoryService.getDeliveryHistories(riderId, dto);
    }
}
