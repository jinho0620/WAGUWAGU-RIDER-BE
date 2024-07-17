package com.example.waguwagu.service;

import com.example.waguwagu.domain.entity.Rider;
import com.example.waguwagu.domain.entity.RiderDeliveryHistory;
import com.example.waguwagu.domain.request.DurationForHistoryDto;
import com.example.waguwagu.domain.request.RiderDeliveryHistoryRequestDto;
import com.example.waguwagu.domain.response.RiderDeliveryHistoryResponseDto;
import com.example.waguwagu.domain.type.RiderTransportation;
import com.example.waguwagu.global.repository.RiderDeliveryHistoryRepository;
import com.example.waguwagu.global.repository.RiderRepository;
import org.junit.jupiter.api.BeforeAll;
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
        RiderDeliveryHistoryRequestDto dto = new RiderDeliveryHistoryRequestDto(
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
            RiderDeliveryHistoryRequestDto dto = new RiderDeliveryHistoryRequestDto(
                    3000, "담미옥");
            riderDeliveryHistoryService.saveDeliveryHistory(savedRider.getRiderId(), dto);
            DurationForHistoryDto durationForHistoryDto = new DurationForHistoryDto(
                    LocalDate.now(),
                    LocalDate.now().plusDays(1));
            List<RiderDeliveryHistoryResponseDto> histories = riderDeliveryHistoryService
                    .getDeliveryHistories(savedRider.getRiderId(), durationForHistoryDto);

            String storeName = histories.get(histories.size()-1).riderStoreName();

            assertNotNull(histories);
            assertEquals("담미옥", storeName);
        }
        @Test
        void success_empty_in_this_duration() {
            RiderDeliveryHistoryRequestDto dto = new RiderDeliveryHistoryRequestDto(
                    3000, "담미옥");
            riderDeliveryHistoryService.saveDeliveryHistory(savedRider.getRiderId(), dto);
            DurationForHistoryDto durationForHistoryDto = new DurationForHistoryDto(
                    LocalDate.now().minusDays(7),
                    LocalDate.now().minusDays(4));

            List<RiderDeliveryHistoryResponseDto> histories = riderDeliveryHistoryService
                    .getDeliveryHistories(savedRider.getRiderId(), durationForHistoryDto);

            assertEquals(0, histories.size());
        }
    }
}