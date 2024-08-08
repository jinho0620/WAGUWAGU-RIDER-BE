package com.example.waguwagu.service;

import com.example.waguwagu.domain.dto.request.RiderLocationRequest;
import com.example.waguwagu.domain.dto.response.RiderLocationResponse;

import java.util.UUID;

public interface RiderLocationService {
    void saveRiderLocation(RiderLocationRequest req);
    RiderLocationResponse getByOrderId(UUID orderId);
    void deleteByOrderId(UUID orderId);
}
