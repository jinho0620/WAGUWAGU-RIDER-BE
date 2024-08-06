package com.example.waguwagu.global.repository;

import com.example.waguwagu.domain.entity.RiderLocation;
import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;

public interface RiderLocationRedisRepository extends ListCrudRepository<RiderLocation, UUID> {
}
