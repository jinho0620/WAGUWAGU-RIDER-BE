package com.example.waguwagu.service;

import com.example.waguwagu.domain.request.DurationForHistoryRequest;
import com.example.waguwagu.domain.request.RiderDeliveryHistoryRequest;
import com.example.waguwagu.domain.response.RiderDeliveryHistoryResponse;

import java.util.List;

public interface RiderDeliveryHistoryService {
    void saveDeliveryHistory(Long riderId, RiderDeliveryHistoryRequest req);
    List<RiderDeliveryHistoryResponse> getDeliveryHistories(Long riderId, DurationForHistoryRequest dto);
}
