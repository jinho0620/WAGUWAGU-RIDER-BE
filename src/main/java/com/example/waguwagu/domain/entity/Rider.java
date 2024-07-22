package com.example.waguwagu.domain.entity;

import com.example.waguwagu.domain.type.RiderTransportation;
import com.example.waguwagu.domain.type.RiderUpdateType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "RIDERS")
public class Rider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RIDER_ID")
    private Long riderId;

    @Setter
    @Column(name = "RIDER_EMAIL")
    private String riderEmail;

    @Setter
    @Column(name = "RIDER_NICKNAME")
    private String riderNickname;

    @Setter
    @Column(name = "RIDER_PHONE_NUMBER")
    private String riderPhoneNumber;

    @Setter
    @Column(name = "RIDER_ACTIVITY_AREA")
    private List<String> riderActivityArea;

    @Setter
    @Column(name = "RIDER_IS_ACTIVE")
    private boolean riderIsActive;

    @Setter
    @Column(name = "RIDER_TRANSPORTATION")
    @Enumerated(EnumType.STRING)
    private RiderTransportation riderTransportation;

    @Setter
    @Column(name = "RIDER_ACCOUNT")
    private String riderAccount;

//    @OneToMany(mappedBy = "rider", cascade = CascadeType.ALL)
    @Setter
    @Column(name = "RIDER_IS_DELETED")
    private boolean riderIsDeleted;

//
}
