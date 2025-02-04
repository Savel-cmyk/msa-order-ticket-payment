package com.travel.application.orderservice.exception;

public class TimeoutWhenWaitingForKafkaRespondException extends RuntimeException {
    public TimeoutWhenWaitingForKafkaRespondException(String message) {
        super(message);
    }
}
