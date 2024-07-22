package com.example.waguwagu.domain.entity;

import com.example.waguwagu.domain.type.RiderTransportation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "DELIVERY_HISTORIES")
public class DeliveryHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderId;
    private String storeName; // 가게 이름
    private int deliveryPay; // 배달 수당
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
