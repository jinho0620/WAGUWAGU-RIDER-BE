package com.example.waguwagu.controller.exceptionController;

import com.example.waguwagu.global.exception.DeliveryHistoryNotFoundException;
import com.example.waguwagu.global.exception.DeliveryRequestNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DeliveryHistoryExceptionController {
    @ExceptionHandler(DeliveryHistoryNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String deliveryHistoryNotFoundExceptionHandler(DeliveryHistoryNotFoundException e) {
        return e.getMessage();
    }

}
