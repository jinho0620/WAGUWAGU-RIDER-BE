package com.example.waguwagu.service;

import com.example.waguwagu.domain.entity.DeliveryHistory;
import com.example.waguwagu.domain.request.DurationForHistoryRequest;
import com.example.waguwagu.domain.request.DeliveryHistoryRequest;
import com.example.waguwagu.domain.response.DeliveryHistoryResponse;
import com.example.waguwagu.domain.type.RiderTransportation;
import com.example.waguwagu.global.dao.RiderDao;
import com.example.waguwagu.global.repository.DeliveryHistoryRepository;
import com.example.waguwagu.kafka.dto.KafkaRiderDto;
import com.example.waguwagu.kafka.KafkaStatus;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DeliveryHistoryServiceTest {
    @Autowired
    private DeliveryHistoryService deliveryHistoryService;
    @Autowired
    private RiderDao riderDao;
    @Autowired
    private DeliveryHistoryRepository deliveryHistoryRepository;
    @Autowired
    private RiderServiceImpl riderServiceImpl;

    @Test
    void saveDeliveryHistory() {
        KafkaStatus<KafkaRiderDto> kafkaStatus = new KafkaStatus<>(
                new KafkaRiderDto(10000L,
                        "wlshzz@naver.com",
                        "Jinho",
                        "123-456-7890",
                        Arrays.asList("노원구", "도봉구", "서초구"),
                        RiderTransportation.BICYCLE,
                        "123-456-789",
                        false), "insert");
        riderServiceImpl.saveRider(kafkaStatus);
        DeliveryHistoryRequest dto = new DeliveryHistoryRequest(
                3000, "담미옥", UUID.fromString("0a12dfd9-8243-468b-9330-1d7cefc70a14"));
        deliveryHistoryService.saveDeliveryHistory(10000L, dto);

        List<DeliveryHistory> histories = deliveryHistoryRepository.findAll();
        DeliveryHistory lastHistory = histories.get(histories.size()-1);

        assertNotNull(lastHistory);
        assertEquals("담미옥", lastHistory.getStoreName());
    }

    @Nested
    class getDeliveryHistories {
        @Test
        void success() {
            DurationForHistoryRequest durationForHistoryRequest = new DurationForHistoryRequest(
                    LocalDate.now(),
                    LocalDate.now().plusDays(1));
            List<DeliveryHistoryResponse> histories = deliveryHistoryService
                    .getDeliveryHistories(10000L, durationForHistoryRequest);

            String storeName = histories.get(histories.size()-1).storeName();

            assertNotNull(histories);
            assertEquals("담미옥", storeName);
        }
        @Test
        void success_empty_in_this_duration() {
            DurationForHistoryRequest durationForHistoryRequest = new DurationForHistoryRequest(
                    LocalDate.now().minusDays(7),
                    LocalDate.now().minusDays(4));

            List<DeliveryHistoryResponse> histories = deliveryHistoryService
                    .getDeliveryHistories(10000L, durationForHistoryRequest);

            assertEquals(0, histories.size());
        }
    }
}