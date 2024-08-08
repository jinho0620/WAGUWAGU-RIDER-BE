package com.example.waguwagu.service;

import com.example.waguwagu.domain.dto.request.RiderLocationRequest;
import com.example.waguwagu.domain.dto.response.RiderLocationResponse;
import com.example.waguwagu.domain.entity.RiderLocation;
import com.example.waguwagu.global.exception.RiderLocationNotFoundException;
import com.example.waguwagu.global.repository.RiderLocationRedisRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RiderLocationServiceImplTest {
    @Autowired
    private RiderLocationService riderLocationService;

    @Autowired
    private RiderLocationRedisRepository riderLocationRedisRepository;

    private final UUID ORDER_ID = UUID.randomUUID();
    @BeforeEach
    void init() {
        RiderLocationRequest req = new RiderLocationRequest(
                ORDER_ID,
                37.6630878, 126.92365493654832, 3627397965L);
        riderLocationRedisRepository.save(req.toEntity());
    }


    @Nested
    @Transactional
    class saveRiderLocation {
        @Test
        @DisplayName("Success : Should fetch data same as one saved")
        void success_save() {
            UUID orderId = UUID.randomUUID();
            RiderLocationRequest req = new RiderLocationRequest(
                    orderId,
                    37.55279861528311, 126.92432158129688, 3627397965L);
            riderLocationService.saveRiderLocation(req);
            RiderLocation location = riderLocationRedisRepository.findById(orderId).orElseThrow(RiderLocationNotFoundException::new);

            assertNotNull(location);
            assertEquals(orderId, location.getId());
            assertEquals(37.55279861528311, location.getRiderLatitude());
            assertEquals(126.92432158129688, location.getRiderLongitude());
        }

        @Test
        @DisplayName("Success : Should update data whose id already exists")
        void success_update() {
            RiderLocationRequest req = new RiderLocationRequest(
                    ORDER_ID,
                    37.55279861528311, 126.92432158129688, 3627397965L);
            riderLocationService.saveRiderLocation(req);
            RiderLocation location = riderLocationRedisRepository.findById(ORDER_ID).orElseThrow(RiderLocationNotFoundException::new);

            assertNotNull(location);
            assertEquals(ORDER_ID, location.getId());
            assertEquals(37.55279861528311, location.getRiderLatitude());
            assertEquals(126.92432158129688, location.getRiderLongitude());
        }

    }

    @Nested
    @Transactional
    class getByOrderId {
        @Test
        @DisplayName("Success : Should fetch data when id exists")
        void success() {
            RiderLocationResponse res = riderLocationService.getByOrderId(ORDER_ID);

            assertNotNull(res);
            assertEquals(37.6630878, res.riderLatitude());
            assertEquals(126.92365493654832, res.riderLongitude());
        }

        @Test
        @DisplayName("Fail : Should throw exception when id doesn't exist")
        void fail() {
            assertThrows(RiderLocationNotFoundException.class
                    , () -> riderLocationService.getByOrderId(UUID.randomUUID()));
        }
    }

    @Nested
    @Transactional
    class deleteByOrderId {
        @Test
        @DisplayName("Success : Should delete data when id exists")
        void success() {
            riderLocationService.deleteByOrderId(ORDER_ID);

            assertThrows(RiderLocationNotFoundException.class, () -> riderLocationService.deleteByOrderId(ORDER_ID));
        }

        @Test
        @DisplayName("Fail : Should throw exception when id doesn't exist")
        void fail() {
            assertThrows(RiderLocationNotFoundException.class
                    , () -> riderLocationService.deleteByOrderId(UUID.randomUUID()));
        }
    }
}