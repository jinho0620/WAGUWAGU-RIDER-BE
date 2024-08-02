package com.example.waguwagu.service;

import com.example.waguwagu.domain.request.DeliveryHistoryDetailRequest;
import com.example.waguwagu.domain.response.DeliveryHistoryDetailResponse;
import com.example.waguwagu.domain.response.DeliveryHistorySummaryResponse;

import java.util.List;

public interface DeliveryHistoryDetailService {
    void saveDeliveryHistoryDetail(Long deliveryHistoryId, DeliveryHistoryDetailRequest req);
    List<DeliveryHistoryDetailResponse> getByDeliveryHistoryId(Long deliveryHistoryId);
    DeliveryHistorySummaryResponse getSummaryByDeliveryHistoryId(Long deliveryHistoryId);
}
