package com.example.waguwagu.domain.entity;

import com.example.waguwagu.domain.type.RiderTransportation;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RedisHash("DeliveryRequest")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@ToString
// 주문 도메인으로 부터 kafka를 통해 받을 데이터
public class DeliveryRequest {
    @Id
    private UUID id;
    private UUID orderId;
    private String storeName; // 가게 이름
    private String storeAddress; // 가게 주소
    private int deliveryPay; // 배달 수당
    private double distanceFromStoreToCustomer; // 가게~고객 거리
    private List<RiderTransportation> transportations; // 라이더 이동 수단 (가게~고객 거리에 따라 결정됨), 0~1km 전부 / 1~2.5km 자전거, 오토바이, 자동차 / 2.5km 이상 : 오토바이, 자동차 /
    private double storeLatitude;
    private double storeLongitude;
    private Timestamp due;
    @Setter
    private boolean assigned;
}


