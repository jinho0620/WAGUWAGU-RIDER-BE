package com.example.waguwagu.service;

import com.example.waguwagu.domain.entity.Rider;
import com.example.waguwagu.domain.dto.request.ChangeActivationStateRequest;
import com.example.waguwagu.domain.type.Transportation;
import com.example.waguwagu.global.dao.RiderDao;
import com.example.waguwagu.global.exception.RiderNotFoundException;
import com.example.waguwagu.kafka.dto.KafkaRiderDto;
import com.example.waguwagu.kafka.KafkaStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RiderServiceTest {
    @Autowired
    private RiderServiceImpl riderServiceImpl;

    @Autowired
    private RiderDao riderDao;

    @BeforeEach
    void initRider() {
        KafkaStatus<KafkaRiderDto> kafkaStatus = new KafkaStatus<>(
                new KafkaRiderDto(1000L,
                        "wlshzz@naver.com",
                        "Jinho",
                        "123-456-7890",
                        Arrays.asList("노원구", "도봉구", "서초구"),
                        Transportation.BICYCLE,
                        "123-456-789",
                        false), "insert");
        riderServiceImpl.saveRider(kafkaStatus);
    }

    @Test
    void saveRider() {
        Rider savedRider = riderDao.findById(1000L);
        assertNotNull(savedRider);
        assertEquals("wlshzz@naver.com", savedRider.getEmail());
    }

    @Test
    void updateRider() {
        KafkaStatus<KafkaRiderDto> kafkaStatus = new KafkaStatus<>(
                new KafkaRiderDto(1000L,
                        "wlshzz@naver.com",
                        "Jinho",
                        "010-4030-9482",
                        Arrays.asList("노원구", "강북구", "서초구", "강남구"),
                        Transportation.MOTORBIKE,
                        "123-456-789",
                        false), "update");

        riderServiceImpl.updateRider(kafkaStatus);
        Rider updatedRider = riderDao.findById(1000L);

        assertNotNull(updatedRider);
        assertEquals("010-4030-9482", updatedRider.getPhoneNumber());
        assertEquals(Transportation.MOTORBIKE, updatedRider.getTransportation());
    }

    @Nested
    class getById {
        @Test
        void success() {
            Long riderId = 1000L;

            Rider getRider = riderServiceImpl.getById(riderId);

            assertNotNull(getRider);
            assertEquals("wlshzz@naver.com", getRider.getEmail());
        }

        @Test
        void fail() {
            assertThrows(RiderNotFoundException.class
                    , () -> riderServiceImpl.getById(100000L));
        }
    }


    @Nested
    class changeActivationState {
        @Test
        void success() {
            Long riderId = 1000L;
            riderServiceImpl.changeActivationState(riderId, new ChangeActivationStateRequest("off"));
            Rider changedRider = riderDao.findById(riderId);
            assertFalse(changedRider.isActive());
        }

        @Test
        void fail() {
            assertThrows(RiderNotFoundException.class
                    , () -> riderServiceImpl.changeActivationState(1000000L, new ChangeActivationStateRequest("on")));
        }

    }
}