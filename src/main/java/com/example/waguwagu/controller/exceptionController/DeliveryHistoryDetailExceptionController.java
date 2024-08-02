package com.example.waguwagu.controller.exceptionController;

import com.example.waguwagu.domain.entity.DeliveryHistoryDetail;
import com.example.waguwagu.global.exception.DeliveryHistoryDetailNotFoundException;
import com.example.waguwagu.global.exception.DeliveryHistoryNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DeliveryHistoryDetailExceptionController {
    @ExceptionHandler(DeliveryHistoryDetailNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String deliveryHistoryDetailNotFoundExceptionHandler(DeliveryHistoryDetailNotFoundException e) {
        return e.getMessage();
    }

}
