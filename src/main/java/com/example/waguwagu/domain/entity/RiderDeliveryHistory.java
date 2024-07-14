package com.example.waguwagu.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "RIDER_DELIVERY_HISTORIES")
public class RiderDeliveryHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RIDER_DELIVERY_HISTORY_ID")
    private Long riderDeliveryHistoryId;

    @Column(name = "RIDER_INCOME")
    private int riderIncome;

    @Column(name = "RIDER_INCOME_CREATED_AT")
    private LocalDateTime riderIncomeCreatedAt;

    @Column(name = "RIDER_STORE_NAME")
    private String riderStoreName;

    @ManyToOne
    @JoinColumn(name = "RIDER_ID")
    private Rider rider;
}
