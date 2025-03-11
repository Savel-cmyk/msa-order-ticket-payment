package com.travel.application.ticketservice.dto;

public interface TicketBookingDtoTemplate {

    String orderId();

    default String bookingStatus() {
        return null;
    }
}
