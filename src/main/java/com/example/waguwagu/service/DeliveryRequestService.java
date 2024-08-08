package com.example.waguwagu.service;

import com.example.waguwagu.domain.dto.request.RiderAssignRequest;
import com.example.waguwagu.domain.dto.response.RiderAssignResponse;

import java.util.List;
import java.util.UUID;

public interface DeliveryRequestService {
    List<RiderAssignResponse> assignRider(Long riderId, RiderAssignRequest req);
    void deleteById(UUID id);
    void updateRiderAssignedAsTrue(UUID id);
}
