package com.example.waguwagu.global.dao;

import com.example.waguwagu.domain.entity.DeliveryHistory;
import com.example.waguwagu.domain.entity.DeliveryHistoryDetail;
import java.util.List;

public interface DeliveryHistoryDetailDao {
    void save(DeliveryHistoryDetail detail);
    List<DeliveryHistoryDetail> findByDeliveryHistoryId(Long deliveryHistoryId);
}
