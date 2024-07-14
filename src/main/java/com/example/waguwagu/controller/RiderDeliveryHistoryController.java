package com.example.waguwagu.controller;

import com.example.waguwagu.domain.request.DurationForHistoryDto;
import com.example.waguwagu.domain.request.RiderDeliveryHistoryRequestDto;
import com.example.waguwagu.domain.response.RiderDeliveryHistoryResponseDto;
import com.example.waguwagu.service.RiderDeliveryHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/deliveryHistories")
public class RiderDeliveryHistoryController {
    private final RiderDeliveryHistoryService riderDeliveryHistoryService;

    @PostMapping("/{riderId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveDeliveryHistory(@PathVariable Long riderId, @RequestBody RiderDeliveryHistoryRequestDto req) {
        riderDeliveryHistoryService.saveDeliveryHistory(riderId, req);
    }

    @GetMapping("/{riderId}")
    public List<RiderDeliveryHistoryResponseDto> getDeliveryHistories(@PathVariable Long riderId,
                                                                      DurationForHistoryDto dto) {
        return riderDeliveryHistoryService.getDeliveryHistories(riderId, dto);
    }
}
