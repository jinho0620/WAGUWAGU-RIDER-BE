package com.example.waguwagu.global.util;

import ch.hsr.geohash.BoundingBox;
import ch.hsr.geohash.GeoHash;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GeoHashUtil {
    private static final int MAX_DELIVERABLE_DISTANCE = 5;
    private static final double LATITUDE_TO_KM = 110.574;
    private static final double LONGITUDE_TO_KM_AT_EQUATOR = 111.320;
    private static final double METER_150_TO_DEGREE = 0.001351;


    public static List<String> coverBoundingBox(BoundingBox boundingBox, int precision) {
        List<String> geoHashes = new ArrayList<>();

        // 라이더 기준 가로, 세로 5km 격자 (bounding box)의 위,경도 가져오기
        double south = boundingBox.getSouthWestCorner().getLatitude();
        double north = boundingBox.getNorthEastCorner().getLatitude();
        double west = boundingBox.getSouthWestCorner().getLongitude();
        double east = boundingBox.getNorthEastCorner().getLongitude();

        // bounding box 안에서 geohash를 확인할 간격 : 150m
        double latStep = METER_150_TO_DEGREE;
        double lonStep = METER_150_TO_DEGREE;

        // bounding box 안에서 150m 간격으로 geoHash 확인
        for (double lat = south; lat <= north; lat += latStep) {
            for (double lon = west; lon <= east; lon += lonStep) {
                GeoHash geoHash = GeoHash.withCharacterPrecision(lat, lon, precision);
                geoHashes.add(geoHash.toBase32());
            }
        }
        return geoHashes;
    }

    public static BoundingBox expandBoundingBox(BoundingBox originalBox, double centerLatitude) {
        // 위,경도를 가로,세로 5km 씩 증가시켜 격자를 만듦 -> 5km 를 degree로 만듦
        double latExpansion = MAX_DELIVERABLE_DISTANCE / LATITUDE_TO_KM;
        double lonExpansion = MAX_DELIVERABLE_DISTANCE
                / (LONGITUDE_TO_KM_AT_EQUATOR * Math.cos(Math.toRadians(centerLatitude)));

        double north = originalBox.getNorthEastCorner().getLatitude() + latExpansion;
        double south = originalBox.getSouthWestCorner().getLatitude() - latExpansion;
        double east = originalBox.getNorthEastCorner().getLongitude() + lonExpansion;
        double west = originalBox.getSouthWestCorner().getLongitude() - lonExpansion;

        return new BoundingBox(south, north, west, east);
    }


}
