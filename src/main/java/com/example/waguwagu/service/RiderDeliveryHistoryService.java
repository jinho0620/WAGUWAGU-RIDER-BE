package com.example.waguwagu.service;

import com.example.waguwagu.domain.request.DurationForHistoryDto;
import com.example.waguwagu.domain.request.RiderDeliveryHistoryRequestDto;
import com.example.waguwagu.domain.response.RiderDeliveryHistoryResponseDto;

import java.util.List;
import java.util.UUID;

public interface RiderDeliveryHistoryService {
    void saveDeliveryHistory(Long riderId, RiderDeliveryHistoryRequestDto req);
    List<RiderDeliveryHistoryResponseDto> getDeliveryHistories(Long riderId, DurationForHistoryDto dto);
}
