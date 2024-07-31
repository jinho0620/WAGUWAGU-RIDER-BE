package com.example.waguwagu.global.dao;


import com.example.waguwagu.domain.entity.DeliveryHistory;
import com.example.waguwagu.global.repository.DeliveryHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DeliveryHistoryDaoImpl implements DeliveryHistoryDao {
    private final DeliveryHistoryRepository deliveryHistoryRepository;
    @Override
    public List<DeliveryHistory> findByIdBetweenTheseDates(Long riderId, LocalDateTime from, LocalDateTime to) {
        return deliveryHistoryRepository
                .findByRider_RiderIdAndRider_RiderIsDeletedFalseAndDeliveryHistoryIsDeletedFalseAndDeliveryIncomeCreatedAtBetween(riderId, from, to);
    }

    @Override
    public void save(DeliveryHistory history) {
        System.out.println("DeliveryHistoryDaoImpl 까지 들어옴");
        deliveryHistoryRepository.save(history);
        System.out.println("DeliveryHistory 저장 완료");
    }
}
