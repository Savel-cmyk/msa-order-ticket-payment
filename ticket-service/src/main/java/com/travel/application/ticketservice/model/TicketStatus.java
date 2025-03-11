package com.travel.application.ticketservice.model;

public enum TicketStatus {
    /**
     * Ticket has not yet been ordered
     */
    AVAILABLE,
    /**
     * Ticket is in process of booking
     */
    PROCESSING,
    /**
     * Ticket has been booked
     */
    BOOKED,
    /**
     * Ticket hasn't been booked 'cause of some payment failure on client side
     */
    PAYMENT_FAILED
}
