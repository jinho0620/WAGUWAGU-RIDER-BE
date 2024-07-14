package com.example.waguwagu.service;

import com.example.waguwagu.domain.entity.Rider;
import com.example.waguwagu.domain.request.RiderUpdateDto;
import com.example.waguwagu.global.repository.RiderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RiderServiceImpl implements RiderService {
    private final RiderRepository riderRepository;

    @Override
    @Transactional
    public void saveRider(Rider rider) {
        riderRepository.save(rider);
    }

    @Override
    @Transactional
    public void updateRider(Long id, RiderUpdateDto req) {
        Rider rider = riderRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        rider.setRiderEmail(req.riderEmail());
        rider.setRiderNickname(req.riderNickname());
        rider.setRiderAccount(req.riderAccount());
        rider.setRiderActivityArea(req.riderActivityArea());
        rider.setRiderPhoneNumber(req.riderPhoneNumber());
        rider.setRiderTransportation(req.riderTransportation());
    }

    @Override
    public Rider getById(Long id) {
        Rider rider = riderRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        return rider;
    }

    @Override
    public void deleteById(Long id) {
        riderRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void changeActivationState(Long id) {
        Rider rider = riderRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        rider.setRiderIsActive(!rider.isRiderIsActive());
    }
}
