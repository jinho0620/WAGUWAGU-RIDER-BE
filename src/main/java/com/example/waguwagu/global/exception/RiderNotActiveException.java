package com.example.waguwagu.global.exception;

public class RiderNotActiveException extends IllegalArgumentException {
    public RiderNotActiveException() {
        super("Rider is not active.");
    }
}
