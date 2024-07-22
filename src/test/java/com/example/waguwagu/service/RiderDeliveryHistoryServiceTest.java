package com.example.waguwagu.service;

import com.example.waguwagu.domain.entity.Rider;
import com.example.waguwagu.domain.entity.RiderDeliveryHistory;
import com.example.waguwagu.domain.request.DurationForHistoryRequest;
import com.example.waguwagu.domain.request.RiderDeliveryHistoryRequest;
import com.example.waguwagu.domain.response.RiderDeliveryHistoryResponse;
import com.example.waguwagu.domain.type.RiderTransportation;
import com.example.waguwagu.global.repository.RiderDeliveryHistoryRepository;
import com.example.waguwagu.global.repository.RiderRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RiderDeliveryHistoryServiceTest {
    @Autowired
    private RiderDeliveryHistoryService riderDeliveryHistoryService;
    @Autowired
    private RiderRepository riderRepository;
    @Autowired
    private RiderDeliveryHistoryRepository riderDeliveryHistoryRepository;


    @Test
    void saveDeliveryHistory() {
        Rider rider = new Rider(
                null,
                "wlshzz@naver.com",
                "Jinho",
                "123-456-7890",
                Arrays.asList("노원구", "도봉구", "서초구"),
                true,
                RiderTransportation.BICYCLE,
                "123-456-789",
                false);
        Rider savedRider = riderRepository.save(rider);
        RiderDeliveryHistoryRequest dto = new RiderDeliveryHistoryRequest(
                3000, "담미옥");
        riderDeliveryHistoryService.saveDeliveryHistory(savedRider.getRiderId(), dto);

        List<RiderDeliveryHistory> histories = riderDeliveryHistoryRepository.findAll();
        RiderDeliveryHistory lastHistory = histories.get(histories.size()-1);

        assertNotNull(lastHistory);
        assertEquals("담미옥", lastHistory.getRiderStoreName());
    }

    @Nested
    class getDeliveryHistories {
        Rider rider = new Rider(
                null,
                "wlshzz@naver.com",
                "Jinho",
                "123-456-7890",
                Arrays.asList("노원구", "도봉구", "서초구"),
                true,
                RiderTransportation.BICYCLE,
                "123-456-789",
                false);
        Rider savedRider = riderRepository.save(rider);
        @Test
        void success() {
            RiderDeliveryHistoryRequest dto = new RiderDeliveryHistoryRequest(
                    3000, "담미옥");
            riderDeliveryHistoryService.saveDeliveryHistory(savedRider.getRiderId(), dto);
            DurationForHistoryRequest durationForHistoryRequest = new DurationForHistoryRequest(
                    LocalDate.now(),
                    LocalDate.now().plusDays(1));
            List<RiderDeliveryHistoryResponse> histories = riderDeliveryHistoryService
                    .getDeliveryHistories(savedRider.getRiderId(), durationForHistoryRequest);

            String storeName = histories.get(histories.size()-1).riderStoreName();

            assertNotNull(histories);
            assertEquals("담미옥", storeName);
        }
        @Test
        void success_empty_in_this_duration() {
            RiderDeliveryHistoryRequest dto = new RiderDeliveryHistoryRequest(
                    3000, "담미옥");
            riderDeliveryHistoryService.saveDeliveryHistory(savedRider.getRiderId(), dto);
            DurationForHistoryRequest durationForHistoryRequest = new DurationForHistoryRequest(
                    LocalDate.now().minusDays(7),
                    LocalDate.now().minusDays(4));

            List<RiderDeliveryHistoryResponse> histories = riderDeliveryHistoryService
                    .getDeliveryHistories(savedRider.getRiderId(), durationForHistoryRequest);

            assertEquals(0, histories.size());
        }
    }
}