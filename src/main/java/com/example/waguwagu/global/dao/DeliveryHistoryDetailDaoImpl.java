package com.example.waguwagu.global.dao;


import com.example.waguwagu.domain.entity.DeliveryHistoryDetail;
import com.example.waguwagu.global.repository.DeliveryHistoryDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeliveryHistoryDetailDaoImpl implements DeliveryHistoryDetailDao {
    private final DeliveryHistoryDetailRepository deliveryHistoryDetailRepository;

    @Override
    public void save(DeliveryHistoryDetail detail) {
        deliveryHistoryDetailRepository.save(detail);
    }

    @Override
    public List<DeliveryHistoryDetail> findByDeliveryHistoryId(Long deliveryHistoryId) {
        return deliveryHistoryDetailRepository
                .findByDeliveryHistory_DeliveryHistoryIdAndDeliveryHistory_DeliveryHistoryIsDeletedFalseAndDeliveryHistoryDetailIsDeletedFalse(deliveryHistoryId);
    }
}
