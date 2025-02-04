package com.travel.application.orderservice.dto;

public record TicketResponseDto(
        String ticketId,
        String cost,
        String date
) {
}
