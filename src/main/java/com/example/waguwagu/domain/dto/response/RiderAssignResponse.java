package com.example.waguwagu.domain.dto.response;

import com.example.waguwagu.domain.entity.DeliveryRequest;
import org.springframework.data.geo.Distance;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

public record RiderAssignResponse(
        UUID reqId,
        UUID orderId,
        String storeName,
        String storeAddress,
        int deliveryPay,
        LocalDateTime due,
        double distanceFromStoreToCustomer,
        double distanceFromStoreToRider,
        double storeLatitude,
        double storeLongitude

) {
    public static RiderAssignResponse from(DeliveryRequest req, Distance distance, int totalCost) {
        RiderAssignResponse res = new RiderAssignResponse(req.getId(),
                req.getOrderId(),
                req.getStoreName(),
                req.getStoreAddress(),
                totalCost,
                req.getDue().toLocalDateTime(),
                req.getDistanceFromStoreToCustomer(),
                Math.floor(distance.getValue()*10)/10,
                req.getStoreLatitude(),
                req.getStoreLongitude());
        return res;
    }
}
