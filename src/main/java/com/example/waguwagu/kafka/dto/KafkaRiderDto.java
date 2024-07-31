package com.example.waguwagu.kafka.dto;

import com.example.waguwagu.domain.entity.Rider;
import com.example.waguwagu.domain.type.RiderTransportation;
import lombok.ToString;

import java.util.List;

public record KafkaRiderDto(
    Long riderId,
    String riderEmail,
    String riderNickname,
    String riderPhoneNumber,
    List<String> riderActivityArea,
    RiderTransportation riderTransportation,
    String riderAccount,
    boolean riderIsDeleted
) {
    public Rider toEntity() {
        Rider rider = Rider.builder()
                .riderId(riderId)
                .riderEmail(riderEmail)
                .riderNickname(riderNickname)
                .riderPhoneNumber(riderPhoneNumber)
                .riderActivityArea(riderActivityArea)
                .riderTransportation(riderTransportation)
                .riderAccount(riderAccount)
                .riderIsDeleted(riderIsDeleted)
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
                ", riderTransportation=" + riderTransportation +
                ", riderAccount='" + riderAccount + '\'' +
                ", riderIsDeleted=" + riderIsDeleted +
                '}';
    }
}
