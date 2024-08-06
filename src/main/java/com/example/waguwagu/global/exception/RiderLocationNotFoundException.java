package com.example.waguwagu.global.exception;

public class RiderLocationNotFoundException extends IllegalArgumentException {
    public RiderLocationNotFoundException() {
        super("RiderLocation is not found");
    }
}
