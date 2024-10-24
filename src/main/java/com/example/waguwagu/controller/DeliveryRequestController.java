package com.example.waguwagu.controller;

//import com.example.waguwagu.service.RiderAssignService;
import com.example.waguwagu.domain.dto.request.RiderAssignRequest;
import com.example.waguwagu.domain.dto.response.NearByOrderResponse;
import com.example.waguwagu.domain.dto.response.RiderAssignResponse;
import com.example.waguwagu.domain.entity.DeliveryRequest;
import com.example.waguwagu.service.DeliveryRequestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/riders/delivery-requests")
@Tag(name = "Delivery Request History")
public class DeliveryRequestController {
    private final DeliveryRequestService deliveryRequestService;

    // @AuthenticationPrincipal is needed (after adding security)
    @PostMapping("/riders/{riderId}/assign")
    @Operation(summary = "Retrieve delivery requests after verifying the qualifications of the rider")
    public List<DeliveryRequest> assignRider(@PathVariable Long riderId, @RequestBody RiderAssignRequest req) throws JsonProcessingException {
        return deliveryRequestService.findNearByOrders(riderId, req);
    }

    // When delivery is completed, delete the request from Redis and save the delivery history in PostgreSQL
    @PostMapping("/delete")
    @Operation(summary = "Delete delivery request by ID")
    public void deleteDeliveryRequest(@RequestBody String deliveryRequestJson) {
        deliveryRequestService.deleteDeliveryRequest(deliveryRequestJson);
    }
}