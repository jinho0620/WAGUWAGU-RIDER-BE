package com.example.waguwagu.domain.entity;

import com.example.waguwagu.domain.type.Transportation;
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
    @Column(name = "ID")
    private Long id;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "NICKNAME")
    private String nickname;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "ACTIVITY_AREA")
    private List<String> activityArea;

    @Column(name = "ACTIVE")
    @Builder.Default
    @Setter
    private boolean active = false;

    @Column(name = "TRANSPORTATION")
    @Enumerated(EnumType.STRING)
    private Transportation transportation;

    @Column(name = "ACCOUNT")
    private String account;

//    @OneToMany(mappedBy = "rider", cascade = CascadeType.ALL)
    @Column(name = "DELETED")
    @Builder.Default
    private boolean deleted = false;

    @Column(name = "CREATED_AT")
    @Builder.Default
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    public void update(KafkaRiderDto dto) {
        if (!dto.riderEmail().equals(this.email)) this.email = dto.riderEmail();
        if (!dto.riderNickname().equals(this.nickname)) this.nickname = dto.riderNickname();
        if (!dto.riderPhoneNumber().equals(this.phoneNumber)) this.phoneNumber = dto.riderPhoneNumber();
        if (!dto.riderActivityArea().equals(this.activityArea)) this.activityArea = dto.riderActivityArea();
        if (!dto.transportation().equals(this.transportation)) this.transportation = dto.transportation();
        if (!dto.riderAccount().equals(this.account)) this.account = dto.riderAccount();
        if (this.deleted != dto.riderIsDeleted()) this.deleted = dto.riderIsDeleted();
    }

    public boolean isNotActive() {
        return !this.active;
    }
}
