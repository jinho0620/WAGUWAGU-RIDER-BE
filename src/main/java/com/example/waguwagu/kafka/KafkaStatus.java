package com.example.waguwagu.kafka;

public record KafkaStatus<T>(
        T data, String status
) {

}
