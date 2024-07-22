package com.example.waguwagu.domain.request;

import java.time.LocalDate;

public record DurationForHistoryRequest(
        LocalDate from,
        LocalDate to
){
}
