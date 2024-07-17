package com.example.waguwagu.global.exception;

public class RiderNotFoundException extends IllegalArgumentException {
    public RiderNotFoundException() {
        super("Rider not found.");
    }
}
