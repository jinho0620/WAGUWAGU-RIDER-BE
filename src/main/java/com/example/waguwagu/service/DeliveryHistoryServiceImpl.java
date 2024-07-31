package com.example.waguwagu.service;

import com.example.waguwagu.domain.entity.DeliveryHistory;
import com.example.waguwagu.domain.entity.Rider;
import com.example.waguwagu.global.dao.DeliveryHistoryDao;
import com.example.waguwagu.domain.request.DurationForHistoryRequest;
import com.example.waguwagu.domain.request.DeliveryHistoryRequest;
import com.example.waguwagu.domain.response.DeliveryHistoryResponse;
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
    public void saveDeliveryHistory(Long riderId, DeliveryHistoryRequest req) {
        Rider rider = riderService.getById(riderId);
        System.out.println("rider 객체 가져오기 완료");
        DeliveryHistory history = req.toEntity(rider);
        System.out.println("history 객체 만들기 완료");
        deliveryHistoryDao.save(history);
    }

    @Override
    public List<DeliveryHistoryResponse> getDeliveryHistories(Long riderId, DurationForHistoryRequest dto) {
        List<DeliveryHistory> histories = deliveryHistoryDao.findByIdBetweenTheseDates(
                riderId,
                dto.from().atStartOfDay(),
                dto.to().plusDays(1).atStartOfDay());
        List<DeliveryHistoryResponse> response = new ArrayList<>();
        histories.forEach(history -> response.add(DeliveryHistoryResponse.from(history)));
        return response;
    }
}
