package com.example.waguwagu.service;

import com.example.waguwagu.domain.entity.DeliveryHistory;
import com.example.waguwagu.global.repository.DeliveryHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryHistoryServiceImpl implements DeliveryHistoryService {
    private final DeliveryHistoryRepository deliveryHistoryRepository;

    @Override
    public void saveHistory(DeliveryHistory deliveryHistory) {
        deliveryHistoryRepository.save(deliveryHistory);
    }
}
