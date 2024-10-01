package com.example.waguwagu.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "DELIVERY_HISTORY_DETAILS")
public class DeliveryHistoryDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "DELIVERY_INCOME")
    private int deliveryIncome;

    @Builder.Default
    @Column(name = "CREATED_AT", unique = true)
    // LocalDateTime으로 변환해서 프론트에 던져줌
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    @Column(name = "STORE_NAME")
    private String storeName;

    @Column(name = "ORDER_ID")
    private UUID orderId;

    @Setter
    @Column(name = "DELETED")
    @Builder.Default
    private boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "DELIVERY_HISTORY_ID")
    private DeliveryHistory deliveryHistory;
}
