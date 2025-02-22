package com.travel.application.orderservice.dto;


public record OrderRequestDto(
        CustomerDto customerDto,
        String number,
        String email
) {
}
