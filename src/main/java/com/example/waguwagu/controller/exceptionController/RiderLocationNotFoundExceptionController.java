package com.example.waguwagu.controller.exceptionController;

import com.example.waguwagu.global.exception.RiderLocationNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RiderLocationNotFoundExceptionController {
    @ExceptionHandler(RiderLocationNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String riderLocationNotFoundExceptionHandler(RiderLocationNotFoundException e) {
        return e.getMessage();
    }

}
