package com.travel.application.orderservice.dto;

public record OrderResponseDto(
        String orderId,
        String customerSNP,
        String number,
        String email,
        String date,
        TicketResponseDto ticketInfo
) {
}
