package com.example.waguwagu.controller;

//import com.example.waguwagu.service.RiderAssignService;
import com.example.waguwagu.domain.request.DeliveryRequestFromKafka;
import com.example.waguwagu.domain.request.RiderAssignRequest;
import com.example.waguwagu.domain.response.RiderAssignResponse;
import com.example.waguwagu.service.DeliveryRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/delivery-requests")
public class DeliveryRequestController {
    private final DeliveryRequestService deliveryRequestService;

    // @AuthenticationPrincipal 필요 (security 추가 후)
    @PostMapping("riders/{riderId}/assign")
    public List<RiderAssignResponse> assignRider(@PathVariable Long riderId, @RequestBody RiderAssignRequest req) {
        return deliveryRequestService.assignRider(riderId, req);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody DeliveryRequestFromKafka dto) {
        deliveryRequestService.save(dto);
    }

    // 배달 완료 되면 redis 의 요청 목록 삭제 및 postgres에 배달 내역 저장
    @PostMapping("/{id}")
    public void deleteFromRedisAndSaveToDatabase(@PathVariable UUID id) {
        deliveryRequestService.deleteFromRedisAndSaveToDatabase(id);
    }


//    @GetMapping
//    public List<DeliveryRequest> getAll() {
//        return deliveryRequestService.getAll();
//    }
}
