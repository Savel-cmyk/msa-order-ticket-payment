package com.travel.application.orderservice.validator;

import com.travel.application.orderservice.annotation.ValidatePhoneFormat;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PhoneFormatValidator implements ConstraintValidator<ValidatePhoneFormat, String> {

    @Override
    public void initialize(ValidatePhoneFormat constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {

        String pattern = "^[0-9]{7,10}$";

        return Pattern.matches(pattern, phoneNumber);
    }
}
