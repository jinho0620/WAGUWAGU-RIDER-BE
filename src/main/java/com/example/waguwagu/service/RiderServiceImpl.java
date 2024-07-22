package com.example.waguwagu.service;

import com.example.waguwagu.domain.entity.Rider;
import com.example.waguwagu.domain.request.ChangeActivationStateRequest;
import com.example.waguwagu.domain.request.RiderUpdateRequest;
import com.example.waguwagu.global.dao.RiderDao;
import com.example.waguwagu.kafka.KafkaStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RiderServiceImpl implements RiderService {
    private final RiderDao riderDao;


//    @KafkaListener(topics = "rider-topic", id = "rider")
//    public void saveRider(KafkaStatus<Rider> kafkaStatus) {
//        log.info("Rider data Received~");
//        if (kafkaStatus.status().equals("insert")) riderDao.save(kafkaStatus.data());
//    }

    public void saveRider(Rider rider) {
//        log.info("Rider data Received~");
        riderDao.save(rider);
    }

    @Override
    @Transactional
    public void updateRider(Long id, RiderUpdateRequest req) {
        Rider rider = riderDao.findById(id);
        rider.setRiderEmail(req.riderEmail());
        rider.setRiderNickname(req.riderNickname());
        rider.setRiderAccount(req.riderAccount());
        rider.setRiderActivityArea(req.riderActivityArea());
        rider.setRiderPhoneNumber(req.riderPhoneNumber());
        rider.setRiderTransportation(req.riderTransportation());
    }

    @Override
    public Rider getById(Long id) {
        Rider rider = riderDao.findById(id);
        return rider;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Rider rider = riderDao.findById(id);
        rider.setRiderIsDeleted(true);
    }

    @Override
    @Transactional
    public void changeActivationState(Long id, ChangeActivationStateRequest req) {
        Rider rider = riderDao.findById(id);
        rider.setRiderIsActive(req.onOff().equals("on"));
    }
}
