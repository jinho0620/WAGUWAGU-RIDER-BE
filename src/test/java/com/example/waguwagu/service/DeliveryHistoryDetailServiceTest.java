package com.example.waguwagu.service;

import com.example.waguwagu.domain.entity.DeliveryHistoryDetail;
import com.example.waguwagu.domain.dto.request.DeliveryHistoryDetailRequest;
import com.example.waguwagu.domain.dto.response.DeliveryHistoryDetailResponse;
import com.example.waguwagu.domain.dto.response.DeliveryHistorySummaryResponse;
import com.example.waguwagu.domain.type.RiderTransportation;
import com.example.waguwagu.global.dao.DeliveryHistoryDetailDao;
import com.example.waguwagu.global.exception.DeliveryHistoryDetailNotFoundException;
import com.example.waguwagu.kafka.KafkaStatus;
import com.example.waguwagu.kafka.dto.KafkaRiderDto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DeliveryHistoryDetailServiceTest {
    @Autowired
    private DeliveryHistoryDetailService deliveryHistoryDetailService;
    @Autowired
    private RiderServiceImpl riderServiceImpl;
    @Autowired
    private DeliveryHistoryService deliveryHistoryService;

    @Autowired
    private DeliveryHistoryDetailDao deliveryHistoryDetailDao;

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
    @DisplayName("Success : save delivery history detail")
    @Transactional
    void saveDeliveryHistoryDetail() {
        Long deliveryHistoryId = deliveryHistoryService.saveDeliveryHistory(200L);
        DeliveryHistoryDetailRequest req = new DeliveryHistoryDetailRequest(
                5000,
                "장꼬방",
                UUID.randomUUID());
        deliveryHistoryDetailService.saveDeliveryHistoryDetail(deliveryHistoryId, req);
        List<DeliveryHistoryDetail> detail = deliveryHistoryDetailDao.findByDeliveryHistoryId(deliveryHistoryId);

        assertNotNull(detail);
        assertNotEquals(0, detail.size());
        assertEquals("장꼬방", detail.get(detail.size()-1).getStoreName());
    }

    @Nested
    @DisplayName("Fetch delivery history detail by delivery history id")
    class getByDeliveryHistoryId {

        @Test
        @DisplayName("Success : should fetch delivery history details")
        @Transactional
        void success() {
            Long deliveryHistoryId = deliveryHistoryService.saveDeliveryHistory(200L);
            DeliveryHistoryDetailRequest req = new DeliveryHistoryDetailRequest(
                    5000,
                    "장꼬방",
                    UUID.randomUUID());
            deliveryHistoryDetailService.saveDeliveryHistoryDetail(deliveryHistoryId, req);

            List<DeliveryHistoryDetailResponse> res = deliveryHistoryDetailService
                    .getByDeliveryHistoryId(deliveryHistoryId);

            assertNotNull(res);
            assertNotEquals(0, res.size());
            assertEquals(5000, res.get(res.size()-1).deliveryIncome());
            assertEquals("장꼬방", res.get(res.size()-1).storeName());
        }

        @Test
        @DisplayName("Success : should return empty list when delivery history detail doesn't exist")
        @Transactional
        void success_empty_detail() {
            Long deliveryHistoryId = deliveryHistoryService.saveDeliveryHistory(200L);
            List<DeliveryHistoryDetailResponse> res = deliveryHistoryDetailService
                    .getByDeliveryHistoryId(deliveryHistoryId);

            assertNotNull(res);
            assertEquals(0, res.size());
        }
    }

    @Nested
    @DisplayName("Fetch total income and delivery count per day")
    class getSummaryByDeliveryHistoryId {
        @Test
        @DisplayName("Success : should fetch total income and delivery count per day")
        @Transactional
        void success() {
            Long deliveryHistoryId = deliveryHistoryService.saveDeliveryHistory(200L);
            DeliveryHistoryDetailRequest req1 = new DeliveryHistoryDetailRequest(
                    5000,
                    "장꼬방",
                    UUID.randomUUID());
            DeliveryHistoryDetailRequest req2 = new DeliveryHistoryDetailRequest(
                    10000,
                    "소반백",
                    UUID.randomUUID());
            DeliveryHistoryDetailRequest req3 = new DeliveryHistoryDetailRequest(
                    3000,
                    "롯데리아",
                    UUID.randomUUID());
            deliveryHistoryDetailService.saveDeliveryHistoryDetail(deliveryHistoryId, req1);
            deliveryHistoryDetailService.saveDeliveryHistoryDetail(deliveryHistoryId, req2);
            deliveryHistoryDetailService.saveDeliveryHistoryDetail(deliveryHistoryId, req3);

            DeliveryHistorySummaryResponse summary = deliveryHistoryDetailService
                    .getSummaryByDeliveryHistoryId(deliveryHistoryId);

            assertNotNull(summary);
            assertEquals(3, summary.deliveryCount());
            assertEquals(18000, summary.deliveryTotalIncome());
        }

        @Test
        @DisplayName("Fail : should throw exception when delivery history detail doesn't exist")
        @Transactional
        void fail() {
            Long deliveryHistoryId = deliveryHistoryService.saveDeliveryHistory(200L);

            assertThrows(DeliveryHistoryDetailNotFoundException.class,
                    () -> deliveryHistoryDetailService
                            .getSummaryByDeliveryHistoryId(deliveryHistoryId));
        }

    }
}