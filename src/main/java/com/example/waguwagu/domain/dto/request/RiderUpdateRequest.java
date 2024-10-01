package com.example.waguwagu.domain.dto.request;

import com.example.waguwagu.domain.type.Transportation;

import java.util.List;

public record RiderUpdateRequest(
    String riderEmail,
    String riderNickname,
    String riderPhoneNumber,
    List<String> riderActivityArea,
    Transportation transportation,
    String riderAccount
) {
}
