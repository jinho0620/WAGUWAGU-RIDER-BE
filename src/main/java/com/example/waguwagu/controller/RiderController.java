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
@Tag(name = "Delivery Rider")
public class RiderController {
    private final RiderService riderService;
    // Receive rider object via Kafka

//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public void saveRider(@RequestBody Rider rider) {
//        riderService.saveRider(rider);
//    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve delivery rider information by ID")
    public Rider getById(@PathVariable Long id) {
        return riderService.getById(id);
    }

    @PutMapping("/{id}/activation")
    @Operation(summary = "Change account activation state")
    public void changeActivationState(@PathVariable Long id, @RequestBody ChangeActivationStateRequest req) {
        riderService.changeActivationState(id, req);
    }
}
