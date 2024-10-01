package com.example.waguwagu.controller;

//import com.example.waguwagu.service.RiderAssignService;
import com.example.waguwagu.domain.dto.request.RiderAssignRequest;
import com.example.waguwagu.domain.dto.response.NearByOrderResponse;
import com.example.waguwagu.domain.dto.response.RiderAssignResponse;
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
@Tag(name = "배달 요청 내역")
public class DeliveryRequestController {
    private final DeliveryRequestService deliveryRequestService;

    // @AuthenticationPrincipal 필요 (security 추가 후)
    @PostMapping("/riders/{riderId}/assign")
    @Operation(summary = "배달 기사의 자격 검증 후 배달 요청 건 가져오기")
    public List<NearByOrderResponse> assignRider(@PathVariable Long riderId, @RequestBody RiderAssignRequest req) throws JsonProcessingException {
        return deliveryRequestService.findNearByOrders(riderId, req);
    }

    // 배달 완료 되면 redis 의 요청 목록 삭제 및 postgres에 배달 내역 저장
    @DeleteMapping("/{id}")
    @Operation(summary = "ID로 배달 요청 건 삭제")
    public void deleteFromRedisById(@PathVariable UUID id) {
        deliveryRequestService.deleteById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "배달 기사의 배달 진행 여부 업데이트")
    public void updateRiderAssigned(@PathVariable UUID id) {
        deliveryRequestService.updateRiderAssigned(id);
    }
}
