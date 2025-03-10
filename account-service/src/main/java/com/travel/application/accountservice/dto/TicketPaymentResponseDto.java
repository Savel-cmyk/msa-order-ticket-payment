package com.travel.application.accountservice.dto;

public record TicketPaymentResponseDto(
        String orderId,
        String bookingStatus,
        String ticketId
) {
}
