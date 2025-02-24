package com.travel.application.paymentservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record CurrencyDto(
        @NotBlank
        @Pattern(
                regexp = "^[A-Z]{3}$",
                message = "Currency name value must contain only 3 letters in upper case"
        )
        String name,
        @NotBlank
        Double exchangeRate,
        @NotEmpty
        String description
) {
}
