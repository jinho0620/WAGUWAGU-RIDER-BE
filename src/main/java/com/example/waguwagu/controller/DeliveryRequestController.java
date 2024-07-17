package com.example.waguwagu.controller;

//import com.example.waguwagu.service.RiderAssignService;
import com.example.waguwagu.domain.entity.DeliveryRequest;
import com.example.waguwagu.domain.request.RiderAssignRequestDto;
import com.example.waguwagu.domain.response.RiderAssignResponseDto;
import com.example.waguwagu.service.DeliveryRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/deliveryRequests")
public class DeliveryRequestController {
    private final DeliveryRequestService deliveryRequestService;

    // @AuthenticationPrincipal 필요 (security 추가 후)
    @PostMapping("riders/{riderId}/assign")
    public List<RiderAssignResponseDto> assignRider(@PathVariable Long riderId, @RequestBody RiderAssignRequestDto req) {
        return deliveryRequestService.assignRider(riderId, req);
    }

//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public void save(@RequestBody DeliveryRequestDto req) {
//        deliveryRequestService.save(req);
//    }

//    @GetMapping
//    public List<DeliveryRequest> getAll() {
//        return deliveryRequestService.getAll();
//    }
}
