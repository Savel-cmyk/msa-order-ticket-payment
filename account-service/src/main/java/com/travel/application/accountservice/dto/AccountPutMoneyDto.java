package com.travel.application.accountservice.dto;

import jakarta.validation.constraints.Pattern;

public record AccountPutMoneyDto(

        @Pattern(regexp = "[0-9]+[.][0-9]{2}")
        String change
) {
}
