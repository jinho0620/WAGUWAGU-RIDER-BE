package com.example.waguwagu.service;

import com.example.waguwagu.domain.request.DurationForHistoryRequest;
import com.example.waguwagu.domain.request.DeliveryHistoryRequest;
import com.example.waguwagu.domain.response.DeliveryHistoryResponse;

import java.util.List;

public interface DeliveryHistoryService {
    void saveDeliveryHistory(Long riderId, DeliveryHistoryRequest req);
    List<DeliveryHistoryResponse> getDeliveryHistories(Long riderId, DurationForHistoryRequest dto);
}
