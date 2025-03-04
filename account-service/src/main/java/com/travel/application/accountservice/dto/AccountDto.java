package com.travel.application.accountservice.dto;

public record AccountDto(
        Double amount,
        CurrencyDto currencyDto
) {
}
