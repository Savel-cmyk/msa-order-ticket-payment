package com.travel.application.ticketservice.controller;

import com.travel.application.ticketservice.dto.TicketRequestDto;
import com.travel.application.ticketservice.dto.TicketResponseDto;
import com.travel.application.ticketservice.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    /**
     * Controller's POST method for saving ticket data to database.
     * @param ticketRequest
     * @return ResponseEntity with http status and ticket data in DTO format
     */
    @PostMapping
    public ResponseEntity<TicketResponseDto> addTicket(
            @RequestBody TicketRequestDto ticketRequest
    ) {
        return new ResponseEntity<>(ticketService.addTicket(ticketRequest),HttpStatus.CREATED);
    }

    /**
     * Controller's GET method for retrieving data about all tickets in DB in DTO format
     * @return ResponseEntity with http status and tickets data in DTO format
     */
    @GetMapping
    public ResponseEntity<List<TicketResponseDto>> getTicket() {
        return new ResponseEntity<>(ticketService.getAllTickets(), HttpStatus.FOUND);
    }
}
