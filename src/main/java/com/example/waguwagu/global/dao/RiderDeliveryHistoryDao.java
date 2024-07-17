package com.example.waguwagu.global.dao;

import com.example.waguwagu.domain.entity.RiderDeliveryHistory;

import java.time.LocalDateTime;
import java.util.List;

public interface RiderDeliveryHistoryDao {
    List<RiderDeliveryHistory> findByIdBetweenTheseDates(Long riderId, LocalDateTime from, LocalDateTime to);
    void save(RiderDeliveryHistory history);
}
