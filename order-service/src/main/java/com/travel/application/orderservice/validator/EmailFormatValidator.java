package com.travel.application.orderservice.validator;

import com.travel.application.orderservice.annotation.ValidateEmailFormat;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class EmailFormatValidator implements ConstraintValidator<ValidateEmailFormat, String> {

    @Override
    public void initialize(ValidateEmailFormat constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {

//        Test cases:
//        0r.4cu.5t@gmail.mail.com - true
//        gu@rubic.ru - true
//        13@out.1 - false
        String pattern = "^([0-9]+[a-zA-Z]+|[a-zA-Z]+[a-zA-Z0-9]*)([.]([0-9]+[a-zA-Z]+|[a-zA-Z]+[a-zA-Z0-9]*)){0,2}" +
                "@[a-zA-Z]+([.][a-zA-Z]+)?[.][a-zA-Z]{2,}$";

        return Pattern.matches(pattern, email);
    }
}
