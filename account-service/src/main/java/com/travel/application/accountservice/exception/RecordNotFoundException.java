package com.travel.application.accountservice.exception;

import lombok.Getter;

/**
 * @author Savel-cmyk
 * @version 0.0.1
 */
@Getter
public class RecordNotFoundException extends RuntimeException {

    private final String recordClassTypeName;

    public RecordNotFoundException(String message, String recordClassTypeName) {
        super(message);
        this.recordClassTypeName =
                recordClassTypeName.split("[.]")[recordClassTypeName.split("[.]").length - 1];
    }
}
