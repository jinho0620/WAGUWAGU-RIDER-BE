package com.example.waguwagu.service;

import com.example.waguwagu.domain.entity.DeliveryRequest;
import com.example.waguwagu.domain.type.RiderTransportation;
import com.example.waguwagu.global.exception.DeliveryRequestNotFoundException;
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

import java.sql.Timestamp;
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
                null,
                37.4868928106781,
                127.023662920981,
                new Timestamp(System.currentTimeMillis()),
                false
                );
            deliveryRequestRedisRepository.save(deliveryRequest);
    }

    @Nested
    @DisplayName("Success : Should fetch data same as one saved")
    @Transactional
    class save {
        @Test
        @DisplayName("Success : when the distance is 0.7km")
        void success_case1() {
            UUID orderId = UUID.randomUUID();
            KafkaDeliveryRequestDto dto = new KafkaDeliveryRequestDto(
                    orderId,
                    "장꼬방묵은김치찌개전문",
                    "서울 서초구 효령로 364",
                    15000,
                    0.7,
                    127.023662920981,
                    37.4868928106781,
                    new Timestamp(System.currentTimeMillis())
            );
            KafkaStatus<KafkaDeliveryRequestDto> kafkaDto = new KafkaStatus<>(dto, "insert");
            deliveryRequestServiceImpl.save(kafkaDto);
            DeliveryRequest req = deliveryRequestRedisRepository.findById(orderId)
                    .orElseThrow(DeliveryRequestNotFoundException::new);

            assertNotNull(req);
            assertEquals(orderId, req.getId());
            assertEquals(Arrays.asList(
                            RiderTransportation.CAR,
                            RiderTransportation.MOTORBIKE,
                            RiderTransportation.BICYCLE,
                            RiderTransportation.WALK
                    ),
                    req.getTransportations());
            assertEquals(37.4868928106781, req.getStoreLatitude());
            assertEquals(127.023662920981, req.getStoreLongitude());
        }

        @Test
        @DisplayName("Success : when the distance is 1km")
        void success_case2() {
            UUID orderId = UUID.randomUUID();
            KafkaDeliveryRequestDto dto = new KafkaDeliveryRequestDto(
                    orderId,
                    "장꼬방묵은김치찌개전문",
                    "서울 서초구 효령로 364",
                    15000,
                    1,
                    127.023662920981,
                    37.4868928106781,
                    new Timestamp(System.currentTimeMillis())
            );
            KafkaStatus<KafkaDeliveryRequestDto> kafkaDto = new KafkaStatus<>(dto, "insert");
            deliveryRequestServiceImpl.save(kafkaDto);
            DeliveryRequest req = deliveryRequestRedisRepository.findById(orderId)
                    .orElseThrow(DeliveryRequestNotFoundException::new);

            assertNotNull(req);
            assertEquals(orderId, req.getId());
            assertEquals(Arrays.asList(
                    RiderTransportation.CAR,
                    RiderTransportation.MOTORBIKE,
                    RiderTransportation.BICYCLE,
                    RiderTransportation.WALK
                    ),
                    req.getTransportations());
            assertEquals(37.4868928106781, req.getStoreLatitude());
            assertEquals(127.023662920981, req.getStoreLongitude());
        }

        @Test
        @DisplayName("Success : when the distance is 2.3km")
        void success_case3() {
            UUID orderId = UUID.randomUUID();
            KafkaDeliveryRequestDto dto = new KafkaDeliveryRequestDto(
                    orderId,
                    "장꼬방묵은김치찌개전문",
                    "서울 서초구 효령로 364",
                    15000,
                    2.3,
                    127.023662920981,
                    37.4868928106781,
                    new Timestamp(System.currentTimeMillis())
            );
            KafkaStatus<KafkaDeliveryRequestDto> kafkaDto = new KafkaStatus<>(dto, "insert");
            deliveryRequestServiceImpl.save(kafkaDto);
            DeliveryRequest req = deliveryRequestRedisRepository.findById(orderId)
                    .orElseThrow(DeliveryRequestNotFoundException::new);

            assertNotNull(req);
            assertEquals(orderId, req.getId());
            assertEquals(Arrays.asList(
                    RiderTransportation.CAR,
                    RiderTransportation.MOTORBIKE,
                    RiderTransportation.BICYCLE
                    ),
                    req.getTransportations());
            assertEquals(37.4868928106781, req.getStoreLatitude());
            assertEquals(127.023662920981, req.getStoreLongitude());
        }

        @Test
        @DisplayName("Success : when the distance is 2.5km")
        void success_case4() {
            UUID orderId = UUID.randomUUID();
            KafkaDeliveryRequestDto dto = new KafkaDeliveryRequestDto(
                    orderId,
                    "장꼬방묵은김치찌개전문",
                    "서울 서초구 효령로 364",
                    15000,
                    2.5,
                    127.023662920981,
                    37.4868928106781,
                    new Timestamp(System.currentTimeMillis())
            );
            KafkaStatus<KafkaDeliveryRequestDto> kafkaDto = new KafkaStatus<>(dto, "insert");
            deliveryRequestServiceImpl.save(kafkaDto);
            DeliveryRequest req = deliveryRequestRedisRepository.findById(orderId)
                    .orElseThrow(DeliveryRequestNotFoundException::new);

            assertNotNull(req);
            assertEquals(orderId, req.getId());
            assertEquals(Arrays.asList(
                    RiderTransportation.CAR,
                    RiderTransportation.MOTORBIKE,
                    RiderTransportation.BICYCLE
            ),
                    req.getTransportations());
            assertEquals(37.4868928106781, req.getStoreLatitude());
            assertEquals(127.023662920981, req.getStoreLongitude());
        }

        @Test
        @DisplayName("Success : when the distance is 4.5km")
        void success_case5() {
            UUID orderId = UUID.randomUUID();
            KafkaDeliveryRequestDto dto = new KafkaDeliveryRequestDto(
                    orderId,
                    "장꼬방묵은김치찌개전문",
                    "서울 서초구 효령로 364",
                    15000,
                    4.5,
                    127.023662920981,
                    37.4868928106781,
                    new Timestamp(System.currentTimeMillis())
            );
            KafkaStatus<KafkaDeliveryRequestDto> kafkaDto = new KafkaStatus<>(dto, "insert");
            deliveryRequestServiceImpl.save(kafkaDto);
            DeliveryRequest req = deliveryRequestRedisRepository.findById(orderId)
                    .orElseThrow(DeliveryRequestNotFoundException::new);

            assertNotNull(req);
            assertEquals(orderId, req.getId());
            assertEquals(Arrays.asList(
                    RiderTransportation.CAR,
                    RiderTransportation.MOTORBIKE
                    )
                    , req.getTransportations());
            assertEquals(37.4868928106781, req.getStoreLatitude());
            assertEquals(127.023662920981, req.getStoreLongitude());
        }
        @Test
        @DisplayName("Success : when the distance is 5km")
        void success_case6() {
            UUID orderId = UUID.randomUUID();
            KafkaDeliveryRequestDto dto = new KafkaDeliveryRequestDto(
                    orderId,
                    "장꼬방묵은김치찌개전문",
                    "서울 서초구 효령로 364",
                    15000,
                    5,
                    127.023662920981,
                    37.4868928106781,
                    new Timestamp(System.currentTimeMillis())
            );
            KafkaStatus<KafkaDeliveryRequestDto> kafkaDto = new KafkaStatus<>(dto, "insert");
            deliveryRequestServiceImpl.save(kafkaDto);
            DeliveryRequest req = deliveryRequestRedisRepository.findById(orderId)
                    .orElseThrow(DeliveryRequestNotFoundException::new);

            assertNotNull(req);
            assertEquals(orderId, req.getId());
            assertEquals(Arrays.asList(
                    RiderTransportation.CAR,
                    RiderTransportation.MOTORBIKE
            ), req.getTransportations());
            assertEquals(37.4868928106781, req.getStoreLatitude());
            assertEquals(127.023662920981, req.getStoreLongitude());
        }
        @Test
        @DisplayName("Success : when the distance is 10km")
        void success_case7() {
            UUID orderId = UUID.randomUUID();
            KafkaDeliveryRequestDto dto = new KafkaDeliveryRequestDto(
                    orderId,
                    "장꼬방묵은김치찌개전문",
                    "서울 서초구 효령로 364",
                    15000,
                    10,
                    127.023662920981,
                    37.4868928106781,
                    new Timestamp(System.currentTimeMillis())
            );
            KafkaStatus<KafkaDeliveryRequestDto> kafkaDto = new KafkaStatus<>(dto, "insert");
            deliveryRequestServiceImpl.save(kafkaDto);
            DeliveryRequest req = deliveryRequestRedisRepository.findById(orderId)
                    .orElseThrow(DeliveryRequestNotFoundException::new);

            assertNotNull(req);
            assertEquals(orderId, req.getId());
            assertNull(req.getTransportations());
            assertEquals(37.4868928106781, req.getStoreLatitude());
            assertEquals(127.023662920981, req.getStoreLongitude());
        }
    }


    @Nested
    class deleteById {
        @Test
        @DisplayName("Success : Should delete data when id exists")
        @Transactional
        void success() {
            deliveryRequestServiceImpl.deleteById(ORDER_ID);

            assertThrows(DeliveryRequestNotFoundException.class
                    , () -> deliveryRequestServiceImpl.deleteById(ORDER_ID));
        }

        @Test
        @DisplayName("Fail : Should throw exception when id doesn't exist")
        @Transactional
        void fail() {
            assertThrows(DeliveryRequestNotFoundException.class
                    , () -> deliveryRequestServiceImpl.deleteById(UUID.randomUUID()));
        }
    }

    @Nested
    class updateRiderAssigned {
        @Test
        @DisplayName("Success : Should update data whose id already exists")
        @Transactional
        void success() {
            deliveryRequestServiceImpl.updateRiderAssigned(ORDER_ID);
            DeliveryRequest req = deliveryRequestRedisRepository.findById(ORDER_ID)
                    .orElseThrow(DeliveryRequestNotFoundException::new);

            assertNotNull(req);
            assertEquals("장꼬방묵은김치찌개전문", req.getStoreName());
            assertTrue(req.isAssigned());
        }

        @Test
        @DisplayName("Fail : Should throw exception when id doesn't exist")
        @Transactional
        void fail() {
            assertThrows(DeliveryRequestNotFoundException.class
                    , () -> deliveryRequestServiceImpl.updateRiderAssigned(UUID.randomUUID()));
        }
    }
}