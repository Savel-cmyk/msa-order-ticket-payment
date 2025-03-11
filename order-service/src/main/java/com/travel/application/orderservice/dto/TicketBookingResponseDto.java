package com.travel.application.orderservice.dto;

public record TicketBookingResponseDto(
        String orderId,
        String bookingStatus,
        String ticketId
) {
}
