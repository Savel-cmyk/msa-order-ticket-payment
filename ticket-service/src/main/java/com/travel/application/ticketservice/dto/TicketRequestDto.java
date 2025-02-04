package com.travel.application.ticketservice.dto;

public record TicketRequestDto(
        String attractionId,
        String cost,
        String date
) {
}
