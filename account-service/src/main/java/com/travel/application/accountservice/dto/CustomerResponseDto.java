package com.travel.application.accountservice.dto;

public record CustomerResponseDto(
        String id,
        String surname,
        String name,
        String username,
        String email
) {
}
