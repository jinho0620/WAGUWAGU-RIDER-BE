package com.example.waguwagu.controller.exceptionController;

import com.example.waguwagu.global.exception.RiderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RiderExceptionController {
    @ExceptionHandler(RiderNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userNotFoundExceptionHandler(RiderNotFoundException e) {
        return e.getMessage();
    }
}
