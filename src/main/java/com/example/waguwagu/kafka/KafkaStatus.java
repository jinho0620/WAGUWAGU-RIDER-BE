package com.example.waguwagu.kafka;

public record KafkaStatus<T>(
        T data, String status
) {
    @Override
    public String toString() {
        return "KafkaStatus{" +
                "data=" + data +
                ", status='" + status + '\'' +
                '}';
    }
}
