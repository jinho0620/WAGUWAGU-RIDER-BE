package com.example.waguwagu.global.repository;

import com.example.waguwagu.domain.entity.DeliveryHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DeliveryHistoryRepository extends JpaRepository<DeliveryHistory, Long> {
    List<DeliveryHistory> findByRider_RiderIdAndRider_RiderIsDeletedFalseAndDeliveryHistoryIsDeletedFalseAndDeliveryIncomeCreatedAtBetween(Long riderId, LocalDateTime from, LocalDateTime to);
}
