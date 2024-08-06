package com.example.waguwagu.global.repository;

import com.example.waguwagu.domain.entity.DeliveryHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DeliveryHistoryRepository extends JpaRepository<DeliveryHistory, Long> {
//    List<DeliveryHistory> findByRider_RiderIdAndRider_RiderIsDeletedFalseAndDeliveryHistoryIsDeletedFalseAndDeliveryIncomeCreatedAtBetween(Long riderId, LocalDateTime from, LocalDateTime to);

    List<DeliveryHistory> findByRider_RiderIdAndRider_RiderIsDeletedFalseAndDeliveryHistoryIsDeletedFalse(Long riderId);

    Optional<DeliveryHistory> findByDeliveryHistoryCreatedAtAndDeliveryHistoryIsDeletedFalse(LocalDate date);
}
