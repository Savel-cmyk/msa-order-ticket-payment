package com.travel.application.orderservice.dto;


public record OrderRequestDto(
        String customerSNP,
        String number,
        String email
) {
}
