package com.example.waguwagu.global.exception;

public class DeliveryRequestNotFoundException extends IllegalArgumentException {
    public DeliveryRequestNotFoundException() {
        super("DeliveryRequest is not found.");
        super.printStackTrace();
    }
}
