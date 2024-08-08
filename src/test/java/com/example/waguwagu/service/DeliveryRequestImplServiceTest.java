package com.example.waguwagu.service;

import com.example.waguwagu.domain.entity.DeliveryRequest;
import com.example.waguwagu.domain.entity.RiderLocation;
import com.example.waguwagu.domain.type.RiderTransportation;
import com.example.waguwagu.global.exception.DeliveryRequestNotFoundException;
import com.example.waguwagu.global.exception.RiderLocationNotFoundException;
import com.example.waguwagu.global.repository.DeliveryRequestRedisRepository;
import com.example.waguwagu.kafka.KafkaStatus;
import com.example.waguwagu.kafka.dto.KafkaDeliveryRequestDto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DeliveryRequestImplServiceTest {
    @Autowired
    private DeliveryRequestServiceImpl deliveryRequestServiceImpl;

    @Autowired
    private DeliveryRequestRedisRepository deliveryRequestRedisRepository;

    private final UUID ORDER_ID = UUID.randomUUID();
    @BeforeEach
    void init() {
        DeliveryRequest deliveryRequest = new DeliveryRequest(
                ORDER_ID,
                ORDER_ID,
                "장꼬방묵은김치찌개전문",
                "서울 서초구 효령로 364",
                15000,
                3.5,
                Arrays.asList(RiderTransportation.WALK, RiderTransportation.BICYCLE, RiderTransportation.MOTORBIKE),
                37.4868928106781,
                127.023662920981,
                LocalDateTime.now(),
                false
                );
        deliveryRequestRedisRepository.save(deliveryRequest);
    }

    @Test
    @DisplayName("Success : Should fetch data same as one saved")
    void save() {
        UUID orderId = UUID.randomUUID();
        KafkaDeliveryRequestDto dto = new KafkaDeliveryRequestDto(
                orderId,
                "장꼬방묵은김치찌개전문",
                "서울 서초구 효령로 364",
                15000,
                3.5,
                127.023662920981,
                37.4868928106781,
                LocalDateTime.now().plusMinutes(30)

        );
        KafkaStatus<KafkaDeliveryRequestDto> kafkaDto = new KafkaStatus<>(dto, "insert");
        deliveryRequestServiceImpl.save(kafkaDto);
        DeliveryRequest req = deliveryRequestRedisRepository.findById(orderId)
                .orElseThrow(DeliveryRequestNotFoundException::new);

        assertNotNull(req);
        assertEquals(orderId, req.getId());
        assertEquals(37.4868928106781, req.getStoreLatitude());
        assertEquals(127.023662920981, req.getStoreLongitude());
    }


    @Nested
    @Transactional
    class deleteById {
        @Test
        @DisplayName("Success : Should delete data when id exists")
        void success() {
            deliveryRequestServiceImpl.deleteById(ORDER_ID);

            assertThrows(DeliveryRequestNotFoundException.class
                    , () -> deliveryRequestServiceImpl.deleteById(ORDER_ID));
        }

        @Test
        @DisplayName("Fail : Should throw exception when id doesn't exist")
        void fail() {
            assertThrows(DeliveryRequestNotFoundException.class
                    , () -> deliveryRequestServiceImpl.deleteById(UUID.randomUUID()));
        }
    }

    @Nested
    @Transactional
    class updateRiderAssignedAsTrue {
        @Test
        @DisplayName("Success : Should update data whose id already exists")
        void success() {
            deliveryRequestServiceImpl.updateRiderAssignedAsTrue(ORDER_ID);
            DeliveryRequest req = deliveryRequestRedisRepository.findById(ORDER_ID)
                    .orElseThrow(DeliveryRequestNotFoundException::new);

            assertNotNull(req);
            assertEquals("장꼬방묵은김치찌개전문", req.getStoreName());
            assertEquals(Arrays.asList(RiderTransportation.WALK, RiderTransportation.BICYCLE, RiderTransportation.MOTORBIKE)
                    , req.getTransportations());
            assertTrue(req.isAssigned());
        }

        @Test
        @DisplayName("Fail : Should throw exception when id doesn't exist")
        void fail() {
            assertThrows(DeliveryRequestNotFoundException.class
                    , () -> deliveryRequestServiceImpl.updateRiderAssignedAsTrue(UUID.randomUUID()));
        }
    }
}