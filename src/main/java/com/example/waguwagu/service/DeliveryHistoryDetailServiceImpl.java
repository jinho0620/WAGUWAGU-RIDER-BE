package com.example.waguwagu.service;

import com.example.waguwagu.domain.entity.DeliveryHistory;
import com.example.waguwagu.domain.entity.DeliveryHistoryDetail;
import com.example.waguwagu.domain.request.DeliveryHistoryDetailRequest;
import com.example.waguwagu.domain.response.DeliveryHistoryDetailResponse;
import com.example.waguwagu.domain.response.DeliveryHistorySummaryResponse;
import com.example.waguwagu.global.dao.DeliveryHistoryDao;
import com.example.waguwagu.global.dao.DeliveryHistoryDetailDao;
import com.example.waguwagu.global.exception.DeliveryHistoryDetailNotFoundException;
import com.example.waguwagu.global.exception.DeliveryHistoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryHistoryDetailServiceImpl implements DeliveryHistoryDetailService {

    private final DeliveryHistoryDetailDao deliveryHistoryDetailDao;
    private final DeliveryHistoryService deliveryHistoryService;


    @Override
    public void saveDeliveryHistoryDetail(Long deliveryHistoryId, DeliveryHistoryDetailRequest req) {
        DeliveryHistory history = deliveryHistoryService.getById(deliveryHistoryId);
        DeliveryHistoryDetail detail = req.toEntity(history);
        deliveryHistoryDetailDao.save(detail);
    }

    @Override
    public List<DeliveryHistoryDetailResponse> getByDeliveryHistoryId(Long deliveryHistoryId) {
        List<DeliveryHistoryDetail> details = deliveryHistoryDetailDao.findByDeliveryHistoryId(deliveryHistoryId);
        List<DeliveryHistoryDetailResponse> res = new ArrayList<>();
        details.forEach(detail -> {
            res.add(DeliveryHistoryDetailResponse.from(detail));
        });
        return res;
    }

    @Override
    public DeliveryHistorySummaryResponse getSummaryByDeliveryHistoryId(Long deliveryHistoryId) {
        List<DeliveryHistoryDetail> details = deliveryHistoryDetailDao.findByDeliveryHistoryId(deliveryHistoryId);
        if (details.isEmpty()) throw new DeliveryHistoryDetailNotFoundException();
        int totalIncome = 0;
        for (DeliveryHistoryDetail detail : details) {
            totalIncome += detail.getDeliveryIncome();
        }
        DeliveryHistorySummaryResponse summary = new DeliveryHistorySummaryResponse(details.size(), totalIncome);
        return summary;
    }
}
