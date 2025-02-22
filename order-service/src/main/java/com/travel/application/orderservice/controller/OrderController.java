package com.travel.application.orderservice.controller;

import com.travel.application.orderservice.dto.OrderRequestDto;
import com.travel.application.orderservice.dto.OrderResponseDto;
import com.travel.application.orderservice.dto.OrderWithoutDetailedTicketInfoResponseDto;
import com.travel.application.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * Controller's POST method for creating order record (pseudo-booking) for requested ticket with ticketId info in
     * actual request and order data in DTO format.
     * @param ticketId
     * @param customerId
     * @return Status code and result of info gathering (as ticket info is placed in another service) and mapping
     */
    @Operation(
            description = "Controller's POST method for creating order record (pseudo-booking) for requested ticket " +
                    "with ticketId info in actual request and order data in DTO format.",
            summary = "Book ticket by creating an order record.",
            responses = {
                    @ApiResponse(
                            description = "Order creation was successful",
                            responseCode = "201"
                    )
            }
    )
    @PostMapping("/{ticketId}/{customerId}")
    public ResponseEntity<OrderWithoutDetailedTicketInfoResponseDto> addOrderForTicket(
            @PathVariable("ticketId") String ticketId,
            @PathVariable("customerId") String customerId
    ) {
        return new ResponseEntity<>(orderService.addOrderForTicket(ticketId, customerId), HttpStatus.CREATED);
    }

    /**
     * Get method for receiving the info on requested orderId from DB.
     * @param orderId
     * @return Status code and result of info gathering
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderWithTicketInfo(
            @PathVariable("orderId") String orderId
    ) {
        return new ResponseEntity<>(orderService.getOrderWithTicketInfo(orderId), HttpStatus.FOUND);
    }
}
