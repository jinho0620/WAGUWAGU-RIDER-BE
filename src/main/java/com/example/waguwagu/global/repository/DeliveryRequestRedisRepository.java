package com.example.waguwagu.global.repository;

import com.example.waguwagu.domain.entity.DeliveryRequest;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryRequestRedisRepository extends ListCrudRepository<DeliveryRequest, UUID> {
}
