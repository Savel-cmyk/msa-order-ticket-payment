package com.travel.application.ticketservice.handler;

import com.travel.application.ticketservice.exception.RecordNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @author Savel-cmyk
 * @version 0.0.1
 */
@RestControllerAdvice
public class ControllerExceptionHandler {

    /**
     * Method for AOP exception handling of type {@code RecordNotFoundException.class}
     *
     * @param exception thrown exception
     * @return records' class type names with corresponding messages in JSON format
     */
    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleRecordNotFound(RecordNotFoundException exception) {

        Map<String, String> errors = new HashMap<>();
        errors.put(exception.getRecordClassTypeName(), exception.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Method for AOP handling exception of type {@code ConstraintViolationException.class}
     *
     * @param exception thrown exception
     * @return parameters' names of DTO where exception has occurred with corresponding describing
     * messages in JSON format
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolation(ConstraintViolationException exception) {

        Map<String, String> errors = exception.getConstraintViolations().stream()
                .flatMap(violation -> {
                    String[] violationParameterName = violation.getPropertyPath()
                            .toString()
                            .split("^.+[.]");
                    return Stream.of(new AbstractMap.SimpleEntry<>(
                            violationParameterName[violationParameterName.length - 1],
                            violation.getMessage()));
                })
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
