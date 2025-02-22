package com.travel.application.orderservice.dto;

public record OrderResponseDto(
        String orderId,
        CustomerDto customerDto,
        String date,
        TicketResponseDto ticketInfo
) {
}
