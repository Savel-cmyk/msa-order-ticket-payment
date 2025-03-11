package com.travel.application.ticketservice.dto.impl;

import com.travel.application.ticketservice.dto.TicketBookingDtoTemplate;

public record TicketPaymentRequestDto(
        String orderId,
        String cost,
        String customerId,
        String ticketId
) implements TicketBookingDtoTemplate {
}
