package com.example.waguwagu.domain.request;

import com.example.waguwagu.domain.type.RiderTransportation;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Setter;

import java.util.List;

public record RiderUpdateDto(
    String riderEmail,
    String riderNickname,
    String riderPhoneNumber,
    List<String> riderActivityArea,
    RiderTransportation riderTransportation,
    String riderAccount
) {
}
