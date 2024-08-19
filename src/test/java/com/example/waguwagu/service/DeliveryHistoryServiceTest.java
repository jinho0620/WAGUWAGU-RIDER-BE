package com.example.waguwagu.service;

import com.example.waguwagu.domain.entity.DeliveryHistory;
import com.example.waguwagu.domain.dto.response.DeliveryHistoryResponse;
import com.example.waguwagu.domain.type.RiderTransportation;
import com.example.waguwagu.global.exception.DeliveryHistoryNotFoundException;
import com.example.waguwagu.global.repository.DeliveryHistoryRepository;
import com.example.waguwagu.kafka.dto.KafkaRiderDto;
import com.example.waguwagu.kafka.KafkaStatus;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DeliveryHistoryServiceTest {
    @Autowired
    private DeliveryHistoryService deliveryHistoryService;
    @Autowired
    private DeliveryHistoryRepository deliveryHistoryRepository;
    @Autowired
    private RiderServiceImpl riderServiceImpl;

    @BeforeEach
    void init() {
        KafkaStatus<KafkaRiderDto> kafkaStatus = new KafkaStatus<>(
                new KafkaRiderDto(200L,
                        "wlshzz@naver.com",
                        "Jinho",
                        "123-456-7890",
                        Arrays.asList("노원구", "도봉구", "서초구"),
                        RiderTransportation.BICYCLE,
                        "123-456-789",
                        false), "insert");
        riderServiceImpl.saveRider(kafkaStatus);
    }

    @Test
    @DisplayName("Success : should return input rider id when saved")
    @Transactional
    void saveDeliveryHistory() {
        Long deliveryHistoryId = deliveryHistoryService.saveDeliveryHistory(200L);
        DeliveryHistory savedHistory = deliveryHistoryRepository.findById(deliveryHistoryId).orElseThrow(DeliveryHistoryNotFoundException::new);

        assertNotNull(savedHistory);
        assertEquals(200L, savedHistory.getRider().getRiderId());
    }

    @Nested
    @DisplayName("Fetch delivery histories by rider id")
    @Transactional
    class getDeliveryHistories {
        @Test
        @DisplayName("Success : should fetch delivery histories")
        void success() {
            Long deliveryHistoryId = deliveryHistoryService.saveDeliveryHistory(200L);
            List<DeliveryHistoryResponse> res = deliveryHistoryService.getDeliveryHistories(200L);

            assertNotNull(res);
            assertNotEquals(0, res.size());
            assertEquals(deliveryHistoryId, res.get(res.size()-1).deliveryHistoryId());
        }
        @Test
        @DisplayName("Success : should return empty list when delivery history doesn't exist")
        void success_empty_history() {
            List<DeliveryHistoryResponse> res = deliveryHistoryService.getDeliveryHistories(200L);

            assertNotNull(res);
            assertEquals(0, res.size());
        }
    }

    @Nested
    @DisplayName("Fetch delivery histories by id")
    @Transactional
    class getById {
        @Test
        @DisplayName("Success : should fetch delivery histories")
        void success() {
            Long deliveryHistoryId = deliveryHistoryService.saveDeliveryHistory(200L);
            DeliveryHistory deliveryHistory = deliveryHistoryService.getById(deliveryHistoryId);

            assertNotNull(deliveryHistory);
            assertEquals(200L, deliveryHistory.getRider().getRiderId());
        }
        @Test
        @DisplayName("Fail : should throw exception when delivery history doesn't exist")
        void fail() {
            assertThrows(DeliveryHistoryNotFoundException.class, () -> deliveryHistoryService.getById(10000000L));
        }
    }

    @Nested
    @Transactional
    class getTodayDeliveryHistory {
        @Test
        @DisplayName("getTodayDeliveryHistory success : should fetch today's delivery history")
        void success() {
            Long deliveryHistoryId = deliveryHistoryService.saveDeliveryHistory(200L);
            DeliveryHistoryResponse res = deliveryHistoryService.getTodayDeliveryHistory(200L);

            assertNotNull(res);
            assertEquals(deliveryHistoryId, res.deliveryHistoryId());
            assertEquals(LocalDate.now(), res.deliveryHistoryCreatedAt());
        }

        @Test
        @DisplayName("getTodayDeliveryHistory fail : should throw exception when delivery history doesn't exist")
        void fail() {
            DeliveryHistoryResponse res = deliveryHistoryService.getTodayDeliveryHistory(5054054L);
            assertNull(res);
        }
    }
}