package com.travel.application.orderservice.dto;

public record OrderWithoutDetailedTicketInfoResponseDto(
        String orderId,
        String customerSNP,
        String number,
        String email,
        String date,
        String ticketId
) {
}
