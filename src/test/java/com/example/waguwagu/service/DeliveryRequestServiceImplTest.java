package com.example.waguwagu.service;

import ch.hsr.geohash.BoundingBox;
import ch.hsr.geohash.GeoHash;
import com.example.waguwagu.domain.dto.request.RiderAssignRequest;
import com.example.waguwagu.domain.dto.response.NearByOrderResponse;
import com.example.waguwagu.domain.dto.response.RiderAssignResponse;
import com.example.waguwagu.domain.entity.DeliveryRequest;
import com.example.waguwagu.domain.entity.Rider;
import com.example.waguwagu.domain.type.Transportation;
import com.example.waguwagu.global.dao.RiderDao;
import com.example.waguwagu.global.exception.DeliveryRequestNotFoundException;
import com.example.waguwagu.global.repository.DeliveryRequestRedisRepository;
import com.example.waguwagu.global.repository.RiderRepository;
import com.example.waguwagu.global.util.GeoHashUtil;
import com.example.waguwagu.kafka.KafkaStatus;
import com.example.waguwagu.kafka.dto.KafkaDeliveryRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StopWatch;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DeliveryRequestServiceImplTest {
    @Autowired
    private DeliveryRequestServiceImpl deliveryRequestServiceImpl;
    @Autowired
    private RiderRepository riderRepository;
    @Autowired
    private RiderDao riderDao;
    @Autowired
    private DeliveryRequestRedisRepository deliveryRequestRedisRepository;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final UUID ORDER_ID = UUID.randomUUID();
    private static final String REDIS_HASH_KEY = "riderLocations";
    private static final int GEO_HASH_PRECISION = 7; // 150m X 150m
    private static final double LATITUDE_TO_KM = 110.574;
    private static final double LONGITUDE_TO_KM_AT_EQUATOR = 111.320;

    @Nested
    @DisplayName("Success : Should fetch data same as one saved")
    @Transactional
    class save {
        @Test
        @DisplayName("Success : when the distance is 0.7km")
        void success_case1() throws JsonProcessingException {
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
            deliveryRequestServiceImpl.saveOrder(kafkaDto);
            Map<Object, Object> storedLocations = redisTemplate.opsForHash().entries(REDIS_HASH_KEY); // Get all stored rider locations from Redis
            System.out.println(storedLocations);
            
            ObjectMapper objectMapper = new ObjectMapper();
            DeliveryRequest savedDeliveryRequest = null;
            for (Map.Entry<Object, Object> entry : storedLocations.entrySet()) {
                System.out.println(entry.getValue().toString());
                System.out.println(entry.getKey().toString());
                DeliveryRequest deliveryRequest = objectMapper.readValue(entry.getKey().toString(), DeliveryRequest.class);
                if (deliveryRequest.getOrderId().equals(orderId)) savedDeliveryRequest = deliveryRequest;
            }


            assertNotNull(savedDeliveryRequest);
            assertEquals(orderId, savedDeliveryRequest.getId());
            assertEquals(Arrays.asList(
                            Transportation.CAR,
                            Transportation.MOTORBIKE,
                            Transportation.BICYCLE,
                            Transportation.WALK
                    ),
                    savedDeliveryRequest.getTransportations());
            assertEquals(37.4868928106781, savedDeliveryRequest.getStoreLatitude());
            assertEquals(127.023662920981, savedDeliveryRequest.getStoreLongitude());
        }

        @Test
        @DisplayName("Success : when the distance is 1km")
        void success_case2() throws JsonProcessingException {
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
            deliveryRequestServiceImpl.saveOrder(kafkaDto);
            Map<Object, Object> storedLocations = redisTemplate.opsForHash().entries(REDIS_HASH_KEY); // Get all stored rider locations from Redis
            ObjectMapper objectMapper = new ObjectMapper();
            DeliveryRequest savedDeliveryRequest = null;
            for (Map.Entry<Object, Object> entry : storedLocations.entrySet()) {
                System.out.println(entry.getValue().toString());
                System.out.println(entry.getKey().toString());
                DeliveryRequest deliveryRequest = objectMapper.readValue(entry.getKey().toString(), DeliveryRequest.class);
                if (deliveryRequest.getOrderId().equals(orderId)) savedDeliveryRequest = deliveryRequest;
            }

            assertNotNull(savedDeliveryRequest);
            assertEquals(orderId, savedDeliveryRequest.getId());
            assertEquals(Arrays.asList(
                    Transportation.CAR,
                    Transportation.MOTORBIKE,
                    Transportation.BICYCLE,
                    Transportation.WALK
                    ),
                    savedDeliveryRequest.getTransportations());
            assertEquals(37.4868928106781, savedDeliveryRequest.getStoreLatitude());
            assertEquals(127.023662920981, savedDeliveryRequest.getStoreLongitude());
        }

        @Test
        @DisplayName("Success : when the distance is 2.3km")
        void success_case3() throws JsonProcessingException {
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
            deliveryRequestServiceImpl.saveOrder(kafkaDto);
            Map<Object, Object> storedLocations = redisTemplate.opsForHash().entries(REDIS_HASH_KEY); // Get all stored rider locations from Redis
            ObjectMapper objectMapper = new ObjectMapper();
            DeliveryRequest savedDeliveryRequest = null;
            for (Map.Entry<Object, Object> entry : storedLocations.entrySet()) {
                System.out.println(entry.getValue().toString());
                System.out.println(entry.getKey().toString());
                DeliveryRequest deliveryRequest = objectMapper.readValue(entry.getKey().toString(), DeliveryRequest.class);
                if (deliveryRequest.getOrderId().equals(orderId)) savedDeliveryRequest = deliveryRequest;
            }


            assertNotNull(savedDeliveryRequest);
            assertEquals(orderId, savedDeliveryRequest.getId());
            assertEquals(Arrays.asList(
                    Transportation.CAR,
                    Transportation.MOTORBIKE,
                    Transportation.BICYCLE
                    ),
                    savedDeliveryRequest.getTransportations());
            assertEquals(37.4868928106781, savedDeliveryRequest.getStoreLatitude());
            assertEquals(127.023662920981, savedDeliveryRequest.getStoreLongitude());
        }

        @Test
        @DisplayName("Success : when the distance is 2.5km")
        void success_case4() throws JsonProcessingException {
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
            deliveryRequestServiceImpl.saveOrder(kafkaDto);
            Map<Object, Object> storedLocations = redisTemplate.opsForHash().entries(REDIS_HASH_KEY); // Get all stored rider locations from Redis
            ObjectMapper objectMapper = new ObjectMapper();
            DeliveryRequest savedDeliveryRequest = null;
            for (Map.Entry<Object, Object> entry : storedLocations.entrySet()) {
                System.out.println(entry.getValue().toString());
                System.out.println(entry.getKey().toString());
                DeliveryRequest deliveryRequest = objectMapper.readValue(entry.getKey().toString(), DeliveryRequest.class);
                if (deliveryRequest.getOrderId().equals(orderId)) savedDeliveryRequest = deliveryRequest;
            }


            assertNotNull(savedDeliveryRequest);
            assertEquals(orderId, savedDeliveryRequest.getId());
            assertEquals(Arrays.asList(
                    Transportation.CAR,
                    Transportation.MOTORBIKE,
                    Transportation.BICYCLE
            ),
                    savedDeliveryRequest.getTransportations());
            assertEquals(37.4868928106781, savedDeliveryRequest.getStoreLatitude());
            assertEquals(127.023662920981, savedDeliveryRequest.getStoreLongitude());
        }

        @Test
        @DisplayName("Success : when the distance is 4.5km")
        void success_case5() throws JsonProcessingException {
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
            deliveryRequestServiceImpl.saveOrder(kafkaDto);
            Map<Object, Object> storedLocations = redisTemplate.opsForHash().entries(REDIS_HASH_KEY); // Get all stored rider locations from Redis

            ObjectMapper objectMapper = new ObjectMapper();
            DeliveryRequest savedDeliveryRequest = null;
            for (Map.Entry<Object, Object> entry : storedLocations.entrySet()) {
                System.out.println(entry.getValue().toString());
                System.out.println(entry.getKey().toString());
                DeliveryRequest deliveryRequest = objectMapper.readValue(entry.getKey().toString(), DeliveryRequest.class);
                if (deliveryRequest.getOrderId().equals(orderId)) savedDeliveryRequest = deliveryRequest;
            }

            assertNotNull(savedDeliveryRequest);
            assertEquals(orderId, savedDeliveryRequest.getId());
            assertEquals(Arrays.asList(
                    Transportation.CAR,
                    Transportation.MOTORBIKE
                    )
                    , savedDeliveryRequest.getTransportations());
            assertEquals(37.4868928106781, savedDeliveryRequest.getStoreLatitude());
            assertEquals(127.023662920981, savedDeliveryRequest.getStoreLongitude());
        }
        @Test
        @DisplayName("Success : when the distance is 5km")
        void success_case6() throws JsonProcessingException {
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
            deliveryRequestServiceImpl.saveOrder(kafkaDto);
            Map<Object, Object> storedLocations = redisTemplate.opsForHash().entries(REDIS_HASH_KEY); // Get all stored rider locations from Redis
            ObjectMapper objectMapper = new ObjectMapper();
            DeliveryRequest savedDeliveryRequest = null;
            for (Map.Entry<Object, Object> entry : storedLocations.entrySet()) {
                System.out.println(entry.getValue().toString());
                System.out.println(entry.getKey().toString());
                DeliveryRequest deliveryRequest = objectMapper.readValue(entry.getKey().toString(), DeliveryRequest.class);
                if (deliveryRequest.getOrderId().equals(orderId)) savedDeliveryRequest = deliveryRequest;
            }


            assertNotNull(savedDeliveryRequest);
            assertEquals(orderId, savedDeliveryRequest.getId());
            assertEquals(Arrays.asList(
                    Transportation.CAR,
                    Transportation.MOTORBIKE
            ), savedDeliveryRequest.getTransportations());
            assertEquals(37.4868928106781, savedDeliveryRequest.getStoreLatitude());
            assertEquals(127.023662920981, savedDeliveryRequest.getStoreLongitude());
        }
        @Test
        @DisplayName("Success : when the distance is 10km")
        void success_case7() throws JsonProcessingException {
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
            deliveryRequestServiceImpl.saveOrder(kafkaDto);
            Map<Object, Object> storedLocations = redisTemplate.opsForHash().entries(REDIS_HASH_KEY); // Get all stored rider locations from Redis

            ObjectMapper objectMapper = new ObjectMapper();
            DeliveryRequest savedDeliveryRequest = null;
            for (Map.Entry<Object, Object> entry : storedLocations.entrySet()) {
                System.out.println(entry.getValue().toString());
                System.out.println(entry.getKey().toString());
                DeliveryRequest deliveryRequest = objectMapper.readValue(entry.getKey().toString(), DeliveryRequest.class);
                if (deliveryRequest.getOrderId().equals(orderId)) savedDeliveryRequest = deliveryRequest;
            }

            assertNotNull(savedDeliveryRequest);
            assertEquals(orderId, savedDeliveryRequest.getId());
            assertEquals(0, savedDeliveryRequest.getTransportations().size());
            assertEquals(37.4868928106781, savedDeliveryRequest.getStoreLatitude());
            assertEquals(127.023662920981, savedDeliveryRequest.getStoreLongitude());
        }
    }


    @Test
    void deleteDeliveryRequest_success() throws JsonProcessingException {
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
        deliveryRequestServiceImpl.saveOrder(kafkaDto);

        Map<Object, Object> storedLocations = redisTemplate.opsForHash().entries(REDIS_HASH_KEY); // Get all stored rider locations from Redis

        ObjectMapper objectMapper = new ObjectMapper();
        DeliveryRequest savedDeliveryRequest = null;
        for (Map.Entry<Object, Object> entry : storedLocations.entrySet()) {
            System.out.println(entry.getValue().toString());
            System.out.println(entry.getKey().toString());
            DeliveryRequest deliveryRequest = objectMapper.readValue(entry.getKey().toString(), DeliveryRequest.class);
            if (deliveryRequest.getOrderId().equals(orderId)) savedDeliveryRequest = deliveryRequest;
        }
        String deliveryRequestJson = objectMapper.writeValueAsString(savedDeliveryRequest);
        deliveryRequestServiceImpl.deleteDeliveryRequest(deliveryRequestJson);

        Map<Object, Object> storedLocationsAfterDelete = redisTemplate.opsForHash().entries(REDIS_HASH_KEY); // Get all stored rider locations from Redis

        DeliveryRequest savedDeliveryRequestAfterDelete = null;
        for (Map.Entry<Object, Object> entry : storedLocationsAfterDelete.entrySet()) {
            DeliveryRequest deliveryRequest = objectMapper.readValue(entry.getKey().toString(), DeliveryRequest.class);
            if (deliveryRequest.getOrderId().equals(orderId)) savedDeliveryRequestAfterDelete = deliveryRequest;
        }

        assertNull(savedDeliveryRequestAfterDelete);

    }

    @Test
    void coverBoundingBox_success() {
        double riderLatitude = 37.654527;
        double riderLongitude = 127.060551;
        GeoHash centerGeoHash = GeoHash.withCharacterPrecision(riderLatitude, riderLongitude, GEO_HASH_PRECISION);
        BoundingBox originalBox = centerGeoHash.getBoundingBox();
        BoundingBox expandedBox = GeoHashUtil.expandBoundingBox(originalBox, riderLatitude);
        List<String> geoHashes = GeoHashUtil.coverBoundingBox(expandedBox, 7);
        System.out.println(geoHashes);
        System.out.println(geoHashes.size());

        assertNotNull(geoHashes);
    }

    @Test
    void expandBoundingBox_success() {
        double riderLatitude = 37.654527;
        double riderLongitude = 127.060551;
        GeoHash centerGeoHash = GeoHash.withCharacterPrecision(riderLatitude, riderLongitude, GEO_HASH_PRECISION);
        System.out.println(centerGeoHash); // 1110011110011001011000101101100111000000000000000000000000000000 -> (37.6556396484375,127.05963134765625) -> (37.654266357421875,127.06100463867188) -> wydq5qf
        BoundingBox originalBox = centerGeoHash.getBoundingBox();
        System.out.println(originalBox); // (37.6556396484375,127.05963134765625) -> (37.654266357421875,127.06100463867188)

        BoundingBox expandedBox = GeoHashUtil.expandBoundingBox(originalBox, riderLatitude);

        double latitudeGap = (expandedBox.getNorthLatitude() - expandedBox.getSouthLatitude())
                * LATITUDE_TO_KM;
        System.out.println(latitudeGap); // 10.151850280761739
        double longitudeGap = (expandedBox.getEastLongitude() - expandedBox.getWestLongitude())
                * (LONGITUDE_TO_KM_AT_EQUATOR * Math.cos(Math.toRadians(riderLatitude)));
        System.out.println(longitudeGap); // 10.121032262659538

        assertEquals(10, (int) latitudeGap);
        assertEquals(10, (int) longitudeGap);
    }

    @Test
    @Transactional
    void performanceTest_geohash() throws JsonProcessingException {
        Rider rider = Rider.builder()
                .id(1000L)
                .email("wlshzz@naver.com")
                .nickname("Jinho")
                .phoneNumber("123-456-7890")
                .activityArea(Arrays.asList("노원구", "도봉구", "서초구"))
                .transportation(Transportation.MOTORBIKE)
                .account("123-456-789")
                .deleted(false)
                .active(true)
                .build();

        riderRepository.save(rider);


        RiderAssignRequest riderAssignRequest = new RiderAssignRequest(127.060551, 37.654527);

        for (int i = 0; i < 3333; i++) {
            KafkaDeliveryRequestDto dto1 = new KafkaDeliveryRequestDto(
                    UUID.randomUUID(),
                    "장꼬방묵은김치찌개전문",
                    "서울 서초구 효령로 364",
                    15000,
                    10,
                    127.0236714,
                    37.48686000000001 ,
                    new Timestamp(System.currentTimeMillis())
            );

            KafkaDeliveryRequestDto dto2 = new KafkaDeliveryRequestDto(
                    UUID.randomUUID(),
                    "수유리 우동집",
                    "서울시 도봉구 덕릉로",
                    20000,
                    3.5,
                    127.03858841582063,
                    37.64198329793004,
                    new Timestamp(System.currentTimeMillis())
            );

            KafkaDeliveryRequestDto dto3 = new KafkaDeliveryRequestDto(
                    UUID.randomUUID(),
                    "건영시네마 팝콘집",
                    "서울시 노원구 동일로",
                    10000,
                    3.5,
                    127.06123427906414,
                    37.646494046602385,
                    new Timestamp(System.currentTimeMillis())
            );

            KafkaStatus<KafkaDeliveryRequestDto> kafkaDto1 = new KafkaStatus<>(dto1, "insert");
            KafkaStatus<KafkaDeliveryRequestDto> kafkaDto2 = new KafkaStatus<>(dto2, "insert");
            KafkaStatus<KafkaDeliveryRequestDto> kafkaDto3 = new KafkaStatus<>(dto3, "insert");

            deliveryRequestServiceImpl.saveOrder(kafkaDto1);
            deliveryRequestServiceImpl.saveOrder(kafkaDto2);
            deliveryRequestServiceImpl.saveOrder(kafkaDto3);
        }
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<DeliveryRequest> res = deliveryRequestServiceImpl.findNearByOrders(1000L, riderAssignRequest);
        stopWatch.stop();
        long elapsedTime = stopWatch.getTotalTimeMillis();
        System.out.println("코드 실행 시간: " + elapsedTime + "밀리초 with geohash");
        System.out.println("배달 건 갯수: " + res.size());

    }
    @Test
    @Transactional
    void performanceTest_calculation() throws JsonProcessingException {

        RiderAssignRequest riderAssignRequest = new RiderAssignRequest(127.060551, 37.654527);
        for (int i = 0; i < 3333; i++) {
            Rider rider = Rider.builder()
                    .id(1000L)
                    .email("wlshzz@naver.com")
                    .nickname("Jinho")
                    .phoneNumber("123-456-7890")
                    .activityArea(Arrays.asList("노원구", "도봉구", "서초구"))
                    .transportation(Transportation.MOTORBIKE)
                    .account("123-456-789")
                    .deleted(false)
                    .active(true)
                    .build();

            riderRepository.save(rider);

            DeliveryRequest deliveryRequest1 = new DeliveryRequest(
                    UUID.randomUUID(),
                    UUID.randomUUID(),
                    "장꼬방묵은김치찌개전문",
                    "서울 서초구 효령로 364",
                    15000,
                    3.5,
                    Arrays.asList(Transportation.CAR,Transportation.MOTORBIKE),
                    37.4868928106781,
                    127.023662920981,
                    new Timestamp(System.currentTimeMillis()),
                    false
            );

            DeliveryRequest deliveryRequest2 = new DeliveryRequest(
                    UUID.randomUUID(),
                    UUID.randomUUID(),
                    "건영시네마 팝콘집",
                    "서울시 노원구 동일로",
                    10000,
                    3.5,
                    Arrays.asList(Transportation.CAR,Transportation.MOTORBIKE),
                    37.646494046602385,
                    127.06123427906414,
                    new Timestamp(System.currentTimeMillis()),
                    false
            );

            DeliveryRequest deliveryRequest3 = new DeliveryRequest(
                    UUID.randomUUID(),
                    UUID.randomUUID(),
                    "수유리 우동집",
                    "서울시 도봉구 덕릉로",
                    20000,
                    3.5,
                    Arrays.asList(Transportation.CAR,Transportation.MOTORBIKE),
                    37.64198329793004,
                    127.03858841582063,
                    new Timestamp(System.currentTimeMillis()),
                    false
            );
            deliveryRequestRedisRepository.save(deliveryRequest1);
            deliveryRequestRedisRepository.save(deliveryRequest2);
            deliveryRequestRedisRepository.save(deliveryRequest3);
        }
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<RiderAssignResponse> res = deliveryRequestServiceImpl.assignRider(1000L, riderAssignRequest);
        stopWatch.stop();
        long elapsedTime = stopWatch.getTotalTimeMillis();
        System.out.println("코드 실행 시간: " + elapsedTime + "밀리초 with geohash");
        System.out.println("배달 건 갯수: " + res.size());
    }
}