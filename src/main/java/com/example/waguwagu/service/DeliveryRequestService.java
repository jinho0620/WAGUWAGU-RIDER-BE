package com.example.waguwagu.service;

import com.example.waguwagu.domain.entity.DeliveryRequest;
import com.example.waguwagu.domain.entity.Rider;
import com.example.waguwagu.domain.request.RiderAssignRequestDto;
import com.example.waguwagu.domain.response.RiderAssignResponseDto;

import java.util.List;

public interface DeliveryRequestService {
//    List<RiderAssignResponseDto> assignRider(Rider rider, RiderAssignRequestDto req);
    List<RiderAssignResponseDto> assignRider(RiderAssignRequestDto req);

    void save(DeliveryRequest req);

    List<DeliveryRequest> getAll();
}
