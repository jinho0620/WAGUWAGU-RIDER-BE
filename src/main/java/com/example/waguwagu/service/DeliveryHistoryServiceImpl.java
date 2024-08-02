package com.example.waguwagu.service;

import com.example.waguwagu.domain.entity.DeliveryHistory;
import com.example.waguwagu.domain.entity.Rider;
import com.example.waguwagu.domain.response.DeliveryHistoryResponse;
import com.example.waguwagu.global.dao.DeliveryHistoryDao;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        System.out.println("rider 객체 가져오기 완료");
        DeliveryHistory history = DeliveryHistory.builder()
                .rider(rider)
                .build();
        System.out.println("history 객체 만들기 완료");
        return deliveryHistoryDao.save(history);
    }

//    @Override
//    public List<Long> getDeliveryHistories(Long riderId, DurationForHistoryRequest dto) {
//        List<DeliveryHistory> histories = deliveryHistoryDao.findByIdBetweenTheseDates(
//                riderId,
//                dto.from().atStartOfDay(),
//                dto.to().plusDays(1).atStartOfDay());
//        List<Long> response = new ArrayList<>();
//        histories.forEach(history -> response.add(history.getDeliveryHistoryId()));
//        return response;
//    }


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
}
