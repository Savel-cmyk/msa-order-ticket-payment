package com.travel.application.orderservice.dto;

import com.travel.application.orderservice.annotation.ValidateEmailFormat;
import com.travel.application.orderservice.annotation.ValidatePhoneFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record CustomerDto(

        @NotBlank
        String surname,
        @NotBlank
        String name,
        @NotEmpty
        String patronymic,
        @ValidateEmailFormat
        @NotEmpty
        String email,
        @ValidatePhoneFormat
        @NotEmpty
        String phoneNumber
) {
}
