package com.travel.application.ticketservice.dto;

public record TicketResponseDto(
        String ticketId,
        String cost,
        String date
) {
}
