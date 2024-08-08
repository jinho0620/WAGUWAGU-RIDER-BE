package com.example.waguwagu.service;

import com.example.waguwagu.domain.dto.request.GeoDistanceRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GeoDistanceServiceImplTest {
    @Autowired
    private GeoDistanceServiceImpl geoDistanceServiceImpl;

    @Test
    void calculateDistance() {
        GeoDistanceRequest req = new GeoDistanceRequest(37.484918, 127.01629, 37.4934705, 127.0142285);
        double distance = geoDistanceServiceImpl.calculateDistance(req);
        System.out.println(distance);
        assertTrue(distance - 1 < 0);
    }
}