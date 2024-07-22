package com.example.waguwagu.service;

import com.example.waguwagu.domain.request.DeliveryRequestFromKafka;
import com.example.waguwagu.domain.request.RiderAssignRequest;
import com.example.waguwagu.domain.response.RiderAssignResponse;

import java.util.List;
import java.util.UUID;

public interface DeliveryRequestService {
//    List<RiderAssignResponseDto> assignRider(Rider rider, RiderAssignRequestDto req);
    List<RiderAssignResponse> assignRider(Long riderId, RiderAssignRequest req);

    void save(DeliveryRequestFromKafka dto);

//    List<DeliveryRequest> getAll();
    void deleteFromRedisAndSaveToDatabase(UUID id);
}
