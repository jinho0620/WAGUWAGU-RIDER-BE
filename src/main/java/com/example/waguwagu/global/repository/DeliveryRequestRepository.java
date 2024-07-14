package com.example.waguwagu.global.repository;

import com.example.waguwagu.domain.entity.DeliveryRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;

public interface DeliveryRequestRepository extends ListCrudRepository<DeliveryRequest, Long> {
}
