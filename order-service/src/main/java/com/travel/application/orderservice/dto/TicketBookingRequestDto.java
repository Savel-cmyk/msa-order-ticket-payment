package com.travel.application.orderservice.dto;

public record TicketBookingRequestDto(
        String orderId,
        String customerId
) {
}
