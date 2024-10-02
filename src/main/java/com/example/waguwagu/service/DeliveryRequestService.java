package com.example.waguwagu.service;

import com.example.waguwagu.domain.dto.request.RiderAssignRequest;
import com.example.waguwagu.domain.dto.response.NearByOrderResponse;
import com.example.waguwagu.domain.dto.response.RiderAssignResponse;
import com.example.waguwagu.domain.entity.DeliveryRequest;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.UUID;

public interface DeliveryRequestService {
    List<DeliveryRequest> findNearByOrders(Long riderId, RiderAssignRequest req) throws JsonProcessingException;
    void deleteDeliveryRequest(String deliveryRequestJson);
}
