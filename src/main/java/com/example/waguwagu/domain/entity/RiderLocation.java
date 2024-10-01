package com.example.waguwagu.domain.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RedisHash("RiderLocation")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@ToString
// 라이더 실시간 위치
public class RiderLocation {
    @Id
    private UUID id;
    private UUID orderId;
    private double riderLatitude;
    private double riderLongitude;
    private Long riderId;
}


