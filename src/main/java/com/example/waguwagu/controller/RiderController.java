package com.example.waguwagu.controller;

import com.example.waguwagu.domain.entity.Rider;
import com.example.waguwagu.domain.dto.request.ChangeActivationStateRequest;
import com.example.waguwagu.service.RiderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/riders/accounts")
@Tag(name = "배달 기사")
public class RiderController {
    private final RiderService riderService;
    // kafka로 rider 객체 받음

//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public void saveRider(@RequestBody Rider rider) {
//        riderService.saveRider(rider);
//    }

    @GetMapping("/{id}")
    @Operation(summary = "ID로 배달 기사 정보 가져오기")
    public Rider getById(@PathVariable Long id) {
        return riderService.getById(id);
    }

    @PutMapping("/{id}/activation")
    @Operation(summary = "계정 활성화 상태 변경")
    public void changeActivationState(@PathVariable Long id, @RequestBody ChangeActivationStateRequest req) {
        riderService.changeActivationState(id, req);
    }
}
