package com.example.waguwagu.global.dao;

import com.example.waguwagu.domain.entity.DeliveryHistory;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface DeliveryHistoryDao {
//    List<DeliveryHistory> findByIdBetweenTheseDates(Long riderId, LocalDateTime from, LocalDateTime to);
    Long save(DeliveryHistory history);
    List<DeliveryHistory> findByRiderId(Long riderId);
    DeliveryHistory findById(Long deliveryHistoryId);
    DeliveryHistory findByCreatedAt(LocalDate date);
    DeliveryHistory findByRiderIdOfToday(Long riderId);
}
