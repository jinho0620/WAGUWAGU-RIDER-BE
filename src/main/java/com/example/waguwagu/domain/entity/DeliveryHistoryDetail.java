package com.example.waguwagu.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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
    @Column(name = "DELIVERY_HISTORY_DETAIL_ID")
    private Long deliveryHistoryDetailId;

    @Column(name = "DELIVERY_INCOME")
    private int deliveryIncome;

    @Builder.Default
    @Column(name = "DELIVERY_HISTORY_DETAIL_CREATED_AT", unique = true)
    // LocalDateTime으로 변환해서 프론트에 던져줌
    private Timestamp deliveryHistoryDetailCreatedAt = new Timestamp(System.currentTimeMillis());

    @Column(name = "STORE_NAME")
    private String storeName;

    @Column(name = "ORDER_ID")
    private UUID orderId;

    @Setter
    @Column(name = "DELIVERY_HISTORY_DETAIL_IS_DELETED")
    @Builder.Default
    private boolean deliveryHistoryDetailIsDeleted = false;

    @ManyToOne
    @JoinColumn(name = "DELIVERY_HISTORY_ID")
    private DeliveryHistory deliveryHistory;
}
