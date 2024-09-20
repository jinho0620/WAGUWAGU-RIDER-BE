package com.example.waguwagu.service;

import com.example.waguwagu.domain.dto.request.DeliveryHistoryDetailRequest;
import com.example.waguwagu.domain.dto.response.DeliveryHistoryDetailResponse;
import com.example.waguwagu.domain.dto.response.DeliveryHistorySummaryResponse;

import java.util.List;

public interface DeliveryHistoryDetailService {
    void saveDeliveryHistoryDetail(Long deliveryHistoryId, DeliveryHistoryDetailRequest req);
    List<DeliveryHistoryDetailResponse> getByDeliveryHistoryId(Long deliveryHistoryId);
    DeliveryHistorySummaryResponse getSummaryByDeliveryHistoryId(Long deliveryHistoryId);
}
