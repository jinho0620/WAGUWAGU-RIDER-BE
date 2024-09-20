package com.example.waguwagu.service;
import com.example.waguwagu.domain.entity.DeliveryHistory;
import com.example.waguwagu.domain.dto.response.DeliveryHistoryResponse;


import java.util.List;

public interface DeliveryHistoryService {
    Long saveDeliveryHistory(Long riderId);
//    List<DeliveryHistoryResponse> getDeliveryHistories(Long riderId, DurationForHistoryRequest dto);
    List<DeliveryHistoryResponse> getDeliveryHistories(Long riderId);
    DeliveryHistory getById(Long deliveryHistoryId);
    DeliveryHistoryResponse getTodayDeliveryHistory(Long riderId);
}

