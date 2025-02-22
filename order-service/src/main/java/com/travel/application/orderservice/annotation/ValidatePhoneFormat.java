package com.travel.application.orderservice.annotation;

import com.travel.application.orderservice.validator.PhoneFormatValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Constraint(validatedBy = PhoneFormatValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidatePhoneFormat {

    String message() default "Phone number format is not valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
