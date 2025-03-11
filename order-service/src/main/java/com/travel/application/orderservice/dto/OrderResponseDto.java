package com.travel.application.orderservice.dto;

public record OrderResponseDto(
        String orderId,
        String date,
        TicketResponseDto ticketInfo
) {
}
