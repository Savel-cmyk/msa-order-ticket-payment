package com.travel.application.orderservice.annotation;

import com.travel.application.orderservice.validator.EmailFormatValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Constraint(validatedBy = EmailFormatValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateEmailFormat {

    String message() default "Email format is not valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
