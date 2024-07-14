package com.example.waguwagu.global.repository;

import com.example.waguwagu.domain.entity.RiderDeliveryHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface RiderDeliveryHistoryRepository extends JpaRepository<RiderDeliveryHistory, Long> {
    List<RiderDeliveryHistory> findByRider_RiderIdAndRiderIncomeCreatedAtBetween(Long riderId, LocalDateTime from, LocalDateTime to);
}
