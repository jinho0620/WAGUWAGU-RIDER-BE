package com.example.waguwagu.service;

import com.example.waguwagu.domain.entity.Rider;
import com.example.waguwagu.domain.dto.request.ChangeActivationStateRequest;
import com.example.waguwagu.global.dao.RiderDao;
import com.example.waguwagu.kafka.dto.KafkaRiderDto;
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


    @KafkaListener(topics = "rider-save-topic", id = "rider-save")
    public void saveRider(KafkaStatus<KafkaRiderDto> kafkaStatus) {
        System.out.println(kafkaStatus.data() + kafkaStatus.status());
        log.info("Rider signup data successfully received for riderId: " + kafkaStatus.data().riderId());
        if (kafkaStatus.status().equals("insert")) riderDao.save(kafkaStatus.data());
    }

    @KafkaListener(topics = "rider-update-topic", id = "rider-update")
    public void updateRider(KafkaStatus<KafkaRiderDto> kafkaStatus) {
        System.out.println(kafkaStatus.data() + kafkaStatus.status());
        log.info("Rider update data successfully received for riderId: " + kafkaStatus.data().riderId());
        if (kafkaStatus.status().equals("update")) riderDao.update(kafkaStatus.data());
    }

//    public void saveRider(Rider rider) {
////        log.info("Rider data Received~");
//        riderDao.save(rider);
//    }

    @Override
    public Rider getById(Long id) {
        Rider rider = riderDao.findById(id);
        return rider;
    }


    @Override
    @Transactional
    public void changeActivationState(Long id, ChangeActivationStateRequest req) {
        Rider rider = riderDao.findById(id);
        rider.setActive(req.onOff().equals("on"));
    }
}
