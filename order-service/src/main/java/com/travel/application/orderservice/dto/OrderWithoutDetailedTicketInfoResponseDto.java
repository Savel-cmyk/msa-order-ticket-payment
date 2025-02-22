package com.travel.application.orderservice.dto;

public record OrderWithoutDetailedTicketInfoResponseDto(
        String orderId,
        CustomerDto customerDto,
        String date,
        String ticketId
) {
}
