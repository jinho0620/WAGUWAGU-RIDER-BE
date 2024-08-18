package com.example.waguwagu.global.dao;


import com.example.waguwagu.domain.entity.DeliveryHistory;
import com.example.waguwagu.global.exception.DeliveryHistoryNotFoundException;
import com.example.waguwagu.global.repository.DeliveryHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DeliveryHistoryDaoImpl implements DeliveryHistoryDao {
    private final DeliveryHistoryRepository deliveryHistoryRepository;
//    @Override
//    public List<DeliveryHistory> findByIdBetweenTheseDates(Long riderId, LocalDateTime from, LocalDateTime to) {
//        return deliveryHistoryRepository
//                .findByRider_RiderIdAndRider_RiderIsDeletedFalseAndDeliveryHistoryIsDeletedFalseAndDeliveryIncomeCreatedAtBetween(riderId, from, to);
//    }

    @Override
    public Long save(DeliveryHistory history) {
        System.out.println("DeliveryHistoryDaoImpl 까지 들어옴");
        DeliveryHistory savedHistory = deliveryHistoryRepository.save(history);
        return savedHistory.getDeliveryHistoryId();
    }

    @Override
    public List<DeliveryHistory> findByRiderId(Long riderId) {
        List<DeliveryHistory> histories = deliveryHistoryRepository.findByRider_RiderIdAndRider_RiderIsDeletedFalseAndDeliveryHistoryIsDeletedFalse(riderId);
        return histories;
    }

    @Override
    public DeliveryHistory findById(Long deliveryHistoryId) {
        return deliveryHistoryRepository.findById(deliveryHistoryId).orElseThrow(DeliveryHistoryNotFoundException::new);
    }

    @Override
    public DeliveryHistory findByCreatedAt(LocalDate date) {
        DeliveryHistory history = deliveryHistoryRepository
                .findByDeliveryHistoryCreatedAtAndDeliveryHistoryIsDeletedFalse(date)
                .orElseThrow(DeliveryHistoryNotFoundException::new);
        return history;
    }

    @Override
    public DeliveryHistory findByRiderIdOfToday(Long riderId) {
        DeliveryHistory history =
                deliveryHistoryRepository
                        .findByRider_RiderIdAndRider_RiderIsDeletedFalseAndDeliveryHistoryIsDeletedFalseAndDeliveryHistoryCreatedAt(riderId, LocalDate.now())
                        .orElseThrow(DeliveryHistoryNotFoundException::new);
        return history;
    }
}
