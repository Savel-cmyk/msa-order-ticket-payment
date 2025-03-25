package com.travel.application.orderservice.controller;

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
     *
     * @param customerId requested customer's unique identifier
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
    //TODO: make app retrieve customer id from security context
    @PostMapping("/{customerId}")
    public ResponseEntity<OrderWithoutDetailedTicketInfoResponseDto> addOrderForTicket(
            @PathVariable("customerId") String customerId
    ) {
        return new ResponseEntity<>(orderService.addOrderForTicket(customerId), HttpStatus.CREATED);
    }

    /**
     * Get method for receiving the info on requested orderId from DB.
     *
     * @param orderId requested order's unique identifier
     * @return Status code and result of info gathering
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderWithTicketInfo(
            @PathVariable("orderId") String orderId
    ) {
        return new ResponseEntity<>(orderService.getOrderWithTicketInfo(orderId), HttpStatus.OK);
    }
}
