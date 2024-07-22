package com.example.waguwagu.service;

import com.example.waguwagu.domain.entity.Rider;
import com.example.waguwagu.domain.entity.RiderDeliveryHistory;
import com.example.waguwagu.global.dao.RiderDeliveryHistoryDao;
import com.example.waguwagu.domain.request.DurationForHistoryRequest;
import com.example.waguwagu.domain.request.RiderDeliveryHistoryRequest;
import com.example.waguwagu.domain.response.RiderDeliveryHistoryResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RiderDeliveryHistoryServiceImpl implements RiderDeliveryHistoryService {
    private final RiderDeliveryHistoryDao riderDeliveryHistoryDao;
    private final RiderService riderService;

    @Override
    @Transactional
    public void saveDeliveryHistory(Long riderId, RiderDeliveryHistoryRequest req) {
        Rider rider = riderService.getById(riderId);
        RiderDeliveryHistory history = req.toEntity(rider);
        riderDeliveryHistoryDao.save(history);
    }

    @Override
    public List<RiderDeliveryHistoryResponse> getDeliveryHistories(Long riderId, DurationForHistoryRequest dto) {
        List<RiderDeliveryHistory> histories = riderDeliveryHistoryDao.findByIdBetweenTheseDates(
                riderId,
                dto.from().atStartOfDay(),
                dto.to().plusDays(1).atStartOfDay());
        List<RiderDeliveryHistoryResponse> response = new ArrayList<>();
        histories.forEach(history -> response.add(RiderDeliveryHistoryResponse.from(history)));
        return response;
    }
}
