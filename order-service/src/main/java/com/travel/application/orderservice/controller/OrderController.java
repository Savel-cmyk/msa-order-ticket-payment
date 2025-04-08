package com.travel.application.orderservice.controller;

import com.travel.application.orderservice.dto.OrderResponseDto;
import com.travel.application.orderservice.dto.OrderWithoutDetailedTicketInfoResponseDto;
import com.travel.application.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * Endpoint for creating order record (pseudo-booking) for requested ticket with ticketId info in
     * actual request and order data in DTO format.
     *
     * @return Status code and result of info gathering (as ticket info is placed in another service) and mapping
     */
    @Operation(
            description = "Endpoint for creating order record (pseudo-booking) for requested ticket with " +
                    "ticketId info in actual request and order data in DTO format.",
            summary = "Book ticket by creating an order record.",
            responses = {
                    @ApiResponse(
                            description = "Order creation was successful",
                            responseCode = "201",
                            content = {
                                    @Content(
                                            schema = @Schema(
                                                    implementation = OrderWithoutDetailedTicketInfoResponseDto.class
                                            ),
                                            examples = {
                                                    @ExampleObject(
                                                            value = "{\n\"orderId\": " +
                                                                    "\"5f5edb1f-44a6-4acb-8587-c88c0fe5b543\",\n" +
                                                                    "\"date\": \"2025-03-27 23:45:10.884973\",\n" +
                                                                    "\"status\": \"1\",\n" +
                                                                    "\"customerId\": " +
                                                                    "\"ecf99a65-a677-45f4-b407-16ef672233fc\",\n"+
                                                                    "\"ticketId\": " +
                                                                    "\"3429f9af-233f-4ea5-bef3-568603d09bd0\"\n}"
                                                    )
                                            }
                                    )
                            }
                    )
            }
    )
    //TODO: make app retrieve customer id from security context
    @PreAuthorize("hasAnyRole('CUSTOMER', 'MANAGER')")
    @PostMapping("/private")
    public ResponseEntity<OrderWithoutDetailedTicketInfoResponseDto> addOrderForTicket() {
        return new ResponseEntity<>(orderService.addOrderForTicket(), HttpStatus.CREATED);
    }

    /**
     * Endpoint for receiving the info on requested orderId from DB.
     *
     * @param orderId requested order's unique identifier
     * @return Status code and result of info gathering
     */
    @Operation(
            description = "Endpoint for receiving the info on requested orderId from DB.",
            summary = "Retrieve order info with more detailed ticket info.",
            parameters = {
                    @Parameter(
                            name = "orderId",
                            description = "requested order's unique identifier",
                            example = "5f5edb1f-44a6-4acb-8587-c88c0fe5b543"
                    )
            },
            responses = {
                    @ApiResponse(
                            description = "Order info with detailed ticket info retrieval was successful",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = OrderResponseDto.class),
                                            examples = {
                                                    @ExampleObject(
                                                            value = "{\n\"orderId\": " +
                                                                    "\"5f5edb1f-44a6-4acb-8587-c88c0fe5b543\",\n" +
                                                                    "\"date\": \"2025-03-27 23:45:10.884973\",\n" +
                                                                    "\"ticketInfo\": {\n\"ticketId\": " +
                                                                    "\"3429f9af-233f-4ea5-bef3-568603d09bd0\",\n" +
                                                                    "\"cost\": \"1000.00\",\n" +
                                                                    "\"date\": \"2025-03-28 10:30:30.000000\"\n}\n}"
                                                    )
                                            }
                                    )
                            }
                    )
            }
    )
    @PreAuthorize("hasAnyRole('CUSTOMER', 'MANAGER')")
    @GetMapping("/private/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderWithTicketInfo(
            @PathVariable("orderId") String orderId
    ) {
        return new ResponseEntity<>(orderService.getOrderWithTicketInfo(orderId), HttpStatus.OK);
    }
}
