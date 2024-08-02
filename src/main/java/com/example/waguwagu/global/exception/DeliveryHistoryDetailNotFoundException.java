package com.example.waguwagu.global.exception;

public class DeliveryHistoryDetailNotFoundException extends IllegalArgumentException {
    public DeliveryHistoryDetailNotFoundException() {
        super("DeliveryHistoryDetail is not found.");
        super.printStackTrace();
    }
}
