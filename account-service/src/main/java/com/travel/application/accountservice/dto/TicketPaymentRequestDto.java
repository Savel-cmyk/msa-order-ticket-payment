package com.travel.application.accountservice.dto;

public record TicketPaymentRequestDto(
        String orderId,
        String cost,
        String customerId,
        String ticketId
) {
}
