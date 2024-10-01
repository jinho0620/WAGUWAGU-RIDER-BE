package com.example.waguwagu.kafka.dto;

import com.example.waguwagu.domain.entity.Rider;
import com.example.waguwagu.domain.type.Transportation;

import java.util.List;

public record KafkaRiderDto(
    Long riderId,
    String riderEmail,
    String riderNickname,
    String riderPhoneNumber,
    List<String> riderActivityArea,
    Transportation transportation,
    String riderAccount,
    boolean riderIsDeleted
) {
    public Rider toEntity() {
        Rider rider = Rider.builder()
                .id(riderId)
                .email(riderEmail)
                .nickname(riderNickname)
                .phoneNumber(riderPhoneNumber)
                .activityArea(riderActivityArea)
                .transportation(transportation)
                .account(riderAccount)
                .deleted(riderIsDeleted)
                .build();
        return rider;
    }

    @Override
    public String toString() {
        return "KafkaRiderDto{" +
                "riderId=" + riderId +
                ", riderEmail='" + riderEmail + '\'' +
                ", riderNickname='" + riderNickname + '\'' +
                ", riderPhoneNumber='" + riderPhoneNumber + '\'' +
                ", riderActivityArea=" + riderActivityArea +
                ", riderTransportation=" + transportation +
                ", riderAccount='" + riderAccount + '\'' +
                ", riderIsDeleted=" + riderIsDeleted +
                '}';
    }
}
