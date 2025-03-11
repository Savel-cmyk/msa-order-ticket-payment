package com.travel.application.orderservice.model;

public enum OrderStatus {
    /**
     * Current order is being paid
     */
    PENDING,
    /**
     * Order has been successfully paid
     */
    CONFIRMED,
    /**
     * Order has been unsuccessfully paid
     */
    CANCELED,
    /**
     * Order has been canceled 'cause of lack of available tickets
     */
    NO_AVAILABLE_TICKETS
}
