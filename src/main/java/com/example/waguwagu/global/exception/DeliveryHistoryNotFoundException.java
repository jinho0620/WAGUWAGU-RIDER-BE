package com.example.waguwagu.global.exception;

public class DeliveryHistoryNotFoundException extends IllegalArgumentException {
    public DeliveryHistoryNotFoundException() {
        super("DeliveryHistory is not found.");
        super.printStackTrace();
    }
}
