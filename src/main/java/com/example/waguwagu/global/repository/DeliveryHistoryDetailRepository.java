package com.example.waguwagu.global.repository;

import com.example.waguwagu.domain.entity.DeliveryHistoryDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DeliveryHistoryDetailRepository extends JpaRepository<DeliveryHistoryDetail, Long> {
    List<DeliveryHistoryDetail> findByDeliveryHistory_IdAndDeliveryHistory_DeletedFalseAndDeletedFalse(Long deliveryHistoryId);
}

