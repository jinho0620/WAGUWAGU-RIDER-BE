package com.example.waguwagu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/riders")
public class HealthCheckController {
    @GetMapping("/health")
    public String isUp() {
        return "up";
    }
}
