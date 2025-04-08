package com.travel.application.ticketservice.controller;

import com.travel.application.ticketservice.dto.TicketRequestDto;
import com.travel.application.ticketservice.dto.TicketResponseDto;
import com.travel.application.ticketservice.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    /**
     * Endpoint for saving ticket data to database.
     *
     * @param ticketRequest
     * @return ResponseEntity with http status and ticket data in DTO format
     * @author Savel-cmyk
     */
    @Operation(
            description = "Endpoint for receiving the info on requested orderId from DB.",
            summary = "Add new ticket to storage.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Ticket data that is requested for storing",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = TicketRequestDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = "{\n\"attractionId\": \"1\",\n" +
                                                            "\"cost\": \"1000.00\",\n" +
                                                            "\"date\": \"2025-03-28 10:30:30.000000\"\n}"
                                            )
                                    }
                            )
                    }
            ),
            responses = {
                    @ApiResponse(
                            description = "Ticket creation was successful",
                            responseCode = "201",
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = TicketRequestDto.class),
                                            examples = {
                                                    @ExampleObject(
                                                            value = "{\n\"ticketId\": " +
                                                                    "\"3429f9af-233f-4ea5-bef3-568603d09bd0\",\n" +
                                                                    "\"cost\": \"1000.00\",\n" +
                                                                    "\"date\": \"2025-03-28 10:30:30.000000\"\n}"
                                                    )
                                            }
                                    )
                            }
                    )
            }
    )
    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/private")
    public ResponseEntity<TicketResponseDto> addTicket(
            @RequestBody TicketRequestDto ticketRequest
    ) {
        return new ResponseEntity<>(ticketService.addTicket(ticketRequest),HttpStatus.CREATED);
    }

    /**
     * Endpoint for retrieving data about all tickets in DB in DTO format.
     *
     * @return ResponseEntity with http status and tickets data in DTO format
     * @author Savel-cmyk
     */
    @Operation(
            description = "Endpoint for retrieving data about all tickets in DB in DTO format.",
            summary = "Retrieve all tickets that are in store.",
            responses = {
                    @ApiResponse(
                            description = "Tickets retrieval was successful",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = TicketResponseDto.class),
                                            examples = {
                                                    @ExampleObject(
                                                            value = "[\n{\n\"ticketId\": " +
                                                                    "\"3429f9af-233f-4ea5-bef3-568603d09bd0\",\n" +
                                                                    "\"cost\": \"1000.00\",\n" +
                                                                    "\"date\": \"2025-03-28 10:30:00.000000\"\n},\n" +
                                                                    "{\n\"ticketId\": " +
                                                                    "\"d7ab2d68-f796-4532-a9ef-4c1243af3177\",\n" +
                                                                    "\"cost\": \"1000.00\",\n" +
                                                                    "\"date\": \"2025-03-28 10:30:30.000000\"\n}\n]"
                                                    )
                                            }
                                    )
                            }
                    )
            }
    )
    @GetMapping("/public")
    public ResponseEntity<List<TicketResponseDto>> getAllTickets() {
        return new ResponseEntity<>(ticketService.getAllTickets(), HttpStatus.OK);
    }
}
