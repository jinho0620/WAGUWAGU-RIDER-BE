package com.example.waguwagu.controller;

//import com.example.waguwagu.service.RiderAssignService;
import com.example.waguwagu.domain.entity.DeliveryRequest;
import com.example.waguwagu.domain.entity.Rider;
import com.example.waguwagu.domain.request.RiderAssignRequestDto;
import com.example.waguwagu.domain.response.RiderAssignResponseDto;
import com.example.waguwagu.service.DeliveryRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/deliveryRequests")
public class DeliveryRequestController {
    private final DeliveryRequestService deliveryRequestService;

//    @PostMapping("/assign")
//    public List<RiderAssignResponseDto> assignRider(Rider rider, @RequestBody RiderAssignRequestDto req) {
//        return deliveryRequestService.assignRider(rider, req);
//    }

    @PostMapping("/assign")
    public List<RiderAssignResponseDto> assignRider(@RequestBody RiderAssignRequestDto req) {
        return deliveryRequestService.assignRider(req);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody DeliveryRequest req) {
        deliveryRequestService.save(req);
    }

    @GetMapping
    public List<DeliveryRequest> getAll() {
        return deliveryRequestService.getAll();
    }
}
