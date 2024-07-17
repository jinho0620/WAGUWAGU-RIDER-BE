package com.example.waguwagu.global.dao;

import com.example.waguwagu.domain.entity.Rider;
import com.example.waguwagu.global.exception.RiderNotFoundException;
import com.example.waguwagu.global.repository.RiderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RiderDaoImpl implements RiderDao {
    private final RiderRepository riderRepository;
    
    @Override
    public Rider findById(Long id) {
        return riderRepository.findByRiderIdAndRiderIsDeletedFalse(id)
                .orElseThrow(RiderNotFoundException::new);
    }

    @Override
    public void save(Rider rider) {
        riderRepository.save(rider);
    }
}
