package com.travel.application.ticketservice.exception;

public class NonExistingEntityRecordException extends RuntimeException{
    public NonExistingEntityRecordException(String message) {
        super(message);
    }
}
