package com.example.waguwagu.global.dao;


import com.example.waguwagu.domain.entity.RiderDeliveryHistory;
import com.example.waguwagu.global.repository.RiderDeliveryHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RiderDeliveryHistoryDaoImpl implements RiderDeliveryHistoryDao {
    private final RiderDeliveryHistoryRepository riderDeliveryHistoryRepository;
    @Override
    public List<RiderDeliveryHistory> findByIdBetweenTheseDates(Long riderId, LocalDateTime from, LocalDateTime to) {
        return riderDeliveryHistoryRepository.findByRider_RiderIdAndRider_RiderIsDeletedFalseAndRiderDeliveryHistoryIsDeletedFalseAndRiderIncomeCreatedAtBetween(riderId, from, to);
    }

    @Override
    public void save(RiderDeliveryHistory history) {
        riderDeliveryHistoryRepository.save(history);
    }
}
