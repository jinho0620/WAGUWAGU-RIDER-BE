package com.example.waguwagu.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @Builder.Default
    @Column(name = "DELIVERY_HISTORY_CREATED_AT")
    private LocalDate deliveryHistoryCreatedAt = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "RIDER_ID")
    private Rider rider;

    @Setter
    @Column(name = "DELIVERY_HISTORY_IS_DELETED")
    @Builder.Default
    private boolean deliveryHistoryIsDeleted = false;
}
