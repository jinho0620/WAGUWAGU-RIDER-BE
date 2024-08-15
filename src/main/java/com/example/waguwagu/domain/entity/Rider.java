package com.example.waguwagu.domain.entity;

import com.example.waguwagu.domain.type.RiderTransportation;
import com.example.waguwagu.kafka.dto.KafkaRiderDto;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "RIDERS")
@ToString
public class Rider {
    @Id
    @Column(name = "RIDER_ID")
    private Long riderId;

    @Column(name = "RIDER_EMAIL")
    private String riderEmail;

    @Column(name = "RIDER_NICKNAME")
    private String riderNickname;

    @Column(name = "RIDER_PHONE_NUMBER")
    private String riderPhoneNumber;

    @Column(name = "RIDER_ACTIVITY_AREA")
    private List<String> riderActivityArea;

    @Column(name = "RIDER_IS_ACTIVE")
    @Builder.Default
    @Setter
    private boolean riderIsActive = false;

    @Column(name = "RIDER_TRANSPORTATION")
    @Enumerated(EnumType.STRING)
    private RiderTransportation riderTransportation;

    @Column(name = "RIDER_ACCOUNT")
    private String riderAccount;

//    @OneToMany(mappedBy = "rider", cascade = CascadeType.ALL)
    @Column(name = "RIDER_IS_DELETED")
    @Builder.Default
    private boolean riderIsDeleted = false;

    @Column(name = "RIDER_CREATED_AT")
    @Builder.Default
    private Timestamp riderCreatedAt = new Timestamp(System.currentTimeMillis());

    public void update(KafkaRiderDto dto) {
        if (!dto.riderEmail().equals(this.riderEmail)) this.riderEmail = dto.riderEmail();
        if (!dto.riderNickname().equals(this.riderNickname)) this.riderNickname = dto.riderNickname();
        if (!dto.riderPhoneNumber().equals(this.riderPhoneNumber)) this.riderPhoneNumber = dto.riderPhoneNumber();
        if (!dto.riderActivityArea().equals(this.riderActivityArea)) this.riderActivityArea = dto.riderActivityArea();
        if (!dto.riderTransportation().equals(this.riderTransportation)) this.riderTransportation = dto.riderTransportation();
        if (!dto.riderAccount().equals(this.riderAccount)) this.riderAccount = dto.riderAccount();
        if (this.riderIsDeleted != dto.riderIsDeleted()) this.riderIsDeleted = dto.riderIsDeleted();
    }
}
