package com.travel.application.ticketservice.dto.impl;

import com.travel.application.ticketservice.dto.TicketBookingDtoTemplate;

public record TicketBookingResponseDto(
        String orderId,
        String bookingStatus,
        String ticketId
) implements TicketBookingDtoTemplate {
}
