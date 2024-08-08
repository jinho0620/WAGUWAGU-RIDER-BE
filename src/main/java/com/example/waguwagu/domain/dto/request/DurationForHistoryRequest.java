package com.example.waguwagu.domain.dto.request;

import java.time.LocalDate;

public record DurationForHistoryRequest(
        LocalDate from,
        LocalDate to
){
}
