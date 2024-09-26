package com.example.waguwagu.domain.dto.request;

import com.example.waguwagu.domain.type.RiderTransportation;

import java.util.List;

public record RiderUpdateRequest(
    String riderEmail,
    String riderNickname,
    String riderPhoneNumber,
    List<String> riderActivityArea,
    RiderTransportation riderTransportation,
    String riderAccount
) {
}
