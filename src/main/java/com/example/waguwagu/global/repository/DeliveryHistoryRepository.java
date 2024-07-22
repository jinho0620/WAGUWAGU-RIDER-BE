package com.example.waguwagu.global.repository;

import com.example.waguwagu.domain.entity.DeliveryHistory;
import com.example.waguwagu.domain.entity.DeliveryRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryHistoryRepository extends JpaRepository<DeliveryHistory, Long> {
}
