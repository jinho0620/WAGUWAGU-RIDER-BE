package com.example.waguwagu.service;

import com.example.waguwagu.domain.entity.Rider;
import com.example.waguwagu.domain.request.ChangeActivationStateRequest;
import com.example.waguwagu.domain.request.RiderUpdateRequest;
import com.example.waguwagu.domain.type.RiderTransportation;
import com.example.waguwagu.global.repository.RiderRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RiderServiceTest {
    @Autowired
    private RiderService riderService;

    @Autowired
    private RiderRepository riderRepository;




//    @Test
//    void saveRider() {
//        Rider rider = new Rider(
//                null,
//                "wlshzz@naver.com",
//                "Jinho",
//                "123-456-7890",
//                Arrays.asList("노원구", "도봉구", "서초구"),
//                true,
//                RiderTransportation.BICYCLE,
//                "123-456-789",
//                false);
//        riderService.saveRider(rider);
//        List<Rider> riders = riderRepository.findAll();
//        Rider savedRider = riders.get(riders.size()-1);
//
//        assertNotNull(savedRider);
//        assertEquals("wlshzz@naver.com", savedRider.getRiderEmail());
//    }

    @Test
    void updateRider() {
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

        RiderUpdateRequest dto = new RiderUpdateRequest(
                "wlshzz@naver.com",
                "Jinho",
                "010-4030-9482",
                Arrays.asList("노원구", "도봉구", "서초구"),
                RiderTransportation.BICYCLE,
                "123-456-789"
                );

        riderService.updateRider(savedRider.getRiderId(), dto);

        Rider updatedRider = riderRepository.findById(savedRider.getRiderId()).orElseThrow();

        assertNotNull(updatedRider);
        assertEquals("010-4030-9482", updatedRider.getRiderPhoneNumber());
    }

    @Nested
    class getById {
        @Test
        void success() {
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

            Rider getRider = riderService.getById(savedRider.getRiderId());

            assertNotNull(getRider);
            assertEquals("wlshzz@naver.com", getRider.getRiderEmail());
        }

        @Test
        void fail() {
            assertThrows(IllegalArgumentException.class , () -> riderService.getById(100000L));
        }




    }

    @Nested
    class deleteById {
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
            riderService.deleteById(savedRider.getRiderId());
            assertThrows(IllegalArgumentException.class , () -> riderService.deleteById(savedRider.getRiderId()));
        }

        @Test
        void fail() {
            assertThrows(IllegalArgumentException.class , () -> riderService.deleteById(100000L));
        }


    }

    @Nested
    class changeActivationState {
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
            riderService.changeActivationState(savedRider.getRiderId(), new ChangeActivationStateRequest("off"));
            Rider changedRider = riderRepository.findById(savedRider.getRiderId()).orElseThrow();
            assertFalse(changedRider.isRiderIsActive());
        }

        @Test
        void fail() {
            assertThrows(IllegalArgumentException.class , () -> riderService.changeActivationState(10000L, new ChangeActivationStateRequest("on")));
        }

    }
}