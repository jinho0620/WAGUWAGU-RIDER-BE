package com.example.waguwagu.controller;

//import com.example.waguwagu.service.RiderAssignService;
import com.example.waguwagu.domain.dto.request.RiderAssignRequest;
import com.example.waguwagu.domain.dto.response.RiderAssignResponse;
import com.example.waguwagu.service.DeliveryRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/delivery-requests")
public class DeliveryRequestController {
    private final DeliveryRequestService deliveryRequestService;

    // @AuthenticationPrincipal 필요 (security 추가 후)
    @PostMapping("/riders/{riderId}/assign")
    public List<RiderAssignResponse> assignRider(@PathVariable Long riderId, @RequestBody RiderAssignRequest req) {
        return deliveryRequestService.assignRider(riderId, req);
    }

    // 배달 완료 되면 redis 의 요청 목록 삭제 및 postgres에 배달 내역 저장
    @DeleteMapping("/{id}")
    public void deleteFromRedisById(@PathVariable UUID id) {
        deliveryRequestService.deleteById(id);
    }

    @PutMapping("/{id}")
    public void updateRiderAssignedAsTrue(@PathVariable UUID id) {
        deliveryRequestService.updateRiderAssignedAsTrue(id);
    }
}
