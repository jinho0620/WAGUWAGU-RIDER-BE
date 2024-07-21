package com.example.waguwagu.domain.request;

import java.time.LocalDate;

public record DurationForHistoryDto(
        LocalDate from,
        LocalDate to
){
}
