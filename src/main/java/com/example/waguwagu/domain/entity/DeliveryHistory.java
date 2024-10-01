package com.example.waguwagu.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "DELIVERY_HISTORIES")
public class DeliveryHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Builder.Default
    @Column(name = "CREATED_AT")
    // LocalDate로 변환해서 던져준다.
    private LocalDate createdAt = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "RIDER_ID")
    private Rider rider;

    @Setter
    @Column(name = "DELETED")
    @Builder.Default
    private boolean deleted = false;
}
