package com.travel.application.orderservice.exception;

public class NonExistingEntityRecordException extends RuntimeException {
    public NonExistingEntityRecordException(String message) {
        super(message);
    }
}
