package com.travel.application.ticketservice.dto.impl;

import com.travel.application.ticketservice.dto.TicketBookingDtoTemplate;

public record TicketBookingRequestDto(
        String orderId,
        String customerId
) implements TicketBookingDtoTemplate {
}
