package com.example.waguwagu.global.dao;

import com.example.waguwagu.domain.entity.Rider;
import com.example.waguwagu.kafka.dto.KafkaRiderDto;

public interface RiderDao {
    Rider findById(Long id);
    void save(KafkaRiderDto dto);
    void update(KafkaRiderDto dto);
}
