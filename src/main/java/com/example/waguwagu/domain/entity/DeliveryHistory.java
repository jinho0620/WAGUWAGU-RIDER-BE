package com.example.waguwagu.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "DELIVERY_HISTORIES")
public class DeliveryHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DELIVERY_HISTORY_ID")
    private Long deliveryHistoryId;

    @Column(name = "DELIVERY_INCOME")
    private int deliveryIncome;

    @Column(name = "STORE_NAME")
    private String storeName;

    @Column(name = "ORDER_ID")
    private UUID orderId;

    @Builder.Default
    @Column(name = "DELIVERY_INCOME_CREATED_AT")
    private LocalDateTime deliveryIncomeCreatedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "RIDER_ID")
    private Rider rider;

    @Setter
    @Column(name = "DELIVERY_HISTORY_IS_DELETED")
    @Builder.Default
    private boolean deliveryHistoryIsDeleted = false;
}
