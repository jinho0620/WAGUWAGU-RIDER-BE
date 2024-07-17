package com.example.waguwagu.global.dao;

import com.example.waguwagu.domain.entity.Rider;

public interface RiderDao {
    Rider findById(Long id);
    void save(Rider rider);
}
