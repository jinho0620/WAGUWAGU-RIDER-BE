package com.example.waguwagu.service;

import com.example.waguwagu.domain.request.RiderLocationRequest;
import com.example.waguwagu.domain.response.RiderLocationResponse;

import java.util.UUID;

public interface RiderLocationService {
    void saveRiderLocation(RiderLocationRequest req);
    RiderLocationResponse getByOrderId(UUID orderId);
    void deleteByOrderId(UUID orderId);


}
