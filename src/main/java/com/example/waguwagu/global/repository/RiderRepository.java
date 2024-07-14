package com.example.waguwagu.global.repository;

import com.example.waguwagu.domain.entity.Rider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RiderRepository extends JpaRepository<Rider, Long> {
}
