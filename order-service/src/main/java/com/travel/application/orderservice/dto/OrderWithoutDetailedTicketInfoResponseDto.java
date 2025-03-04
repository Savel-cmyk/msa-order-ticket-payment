package com.travel.application.orderservice.dto;

public record OrderWithoutDetailedTicketInfoResponseDto(
        String orderId,
        String date,
        String ticketId
) {
}
