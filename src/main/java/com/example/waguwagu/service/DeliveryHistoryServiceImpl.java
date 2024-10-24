package com.example.waguwagu.service;

import com.example.waguwagu.domain.entity.DeliveryHistory;
import com.example.waguwagu.domain.entity.Rider;
import com.example.waguwagu.domain.dto.response.DeliveryHistoryResponse;
import com.example.waguwagu.global.dao.DeliveryHistoryDao;
import com.example.waguwagu.global.exception.DeliveryHistoryNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryHistoryServiceImpl implements DeliveryHistoryService {
    private final DeliveryHistoryDao deliveryHistoryDao;
    private final RiderService riderService;

    @Override
    @Transactional
    public Long saveDeliveryHistory(Long riderId) {
        Rider rider = riderService.getById(riderId);
        try {
            DeliveryHistory history = deliveryHistoryDao.findByCreatedAt(LocalDate.now());
            return history.getId();
        } catch(DeliveryHistoryNotFoundException e) {
            DeliveryHistory history = DeliveryHistory.builder()
                    .rider(rider)
                    .build();
            return deliveryHistoryDao.save(history);
        }
    }

    @Override
    public List<DeliveryHistoryResponse> getDeliveryHistories(Long riderId) {
        List<DeliveryHistory> histories = deliveryHistoryDao.findByRiderId(riderId);
        List<DeliveryHistoryResponse> res = new ArrayList<>();
        histories.forEach(history -> res.add(DeliveryHistoryResponse.from(history)));
        return res;
    }

    @Override
    public DeliveryHistory getById(Long deliveryHistoryId) {
        return deliveryHistoryDao.findById(deliveryHistoryId);
    }

    @Override
    public DeliveryHistoryResponse getTodayDeliveryHistory(Long riderId) {
        try {
            DeliveryHistory history = deliveryHistoryDao.findByRiderIdOfToday(riderId);
            DeliveryHistoryResponse res = DeliveryHistoryResponse.from(history);
            return res;
        } catch (DeliveryHistoryNotFoundException e) {
            return null;
        }
    }
}
