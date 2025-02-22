package com.travel.application.ticketservice.service;

import com.travel.application.ticketservice.dto.TicketRequestDto;
import com.travel.application.ticketservice.dto.TicketResponseDto;
import com.travel.application.ticketservice.exception.RecordNotFoundException;
import com.travel.application.ticketservice.mapper.TicketMapper;
import com.travel.application.ticketservice.model.Ticket;
import com.travel.application.ticketservice.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    /**
     * Method searches record of ticket with requested id in database and then converts in to DTO format to return
     * where it needs.
     * @param ticketId
     * @return ticket response in DTO format with adjusted field values
     */
    public TicketResponseDto getTicketInfo(String ticketId) {

        Ticket ticket = ticketRepository.findById(UUID.fromString(ticketId))
                .orElseThrow(() ->
                        new RecordNotFoundException(
                                "Couldn't find ticket record with such id in database.",
                                Ticket.class.getTypeName()
                        )
                );

        return ticketMapper.toTicketResponseDto(ticket);
    }

    /**
     * Method for saving to database requested data and converting persisted data to DTO format.
     * @param ticketRequest
     * @return ticket data in TicketResponseDto format
     */
    public TicketResponseDto addTicket(TicketRequestDto ticketRequest) {

        Ticket ticket = ticketMapper.toTicket(ticketRequest);
        Ticket persistedTicket = ticketRepository.save(ticket);
        return ticketMapper.toTicketResponseDto(persistedTicket);
    }

    /**
     * Method for retrieving all tickets data from DB and applying map to DTO methods.
     * @return list of all tickets in DTO format that DB contains
     */
    public List<TicketResponseDto> getAllTickets() {

        List<Ticket> tickets = ticketRepository.findAll();
        return tickets.stream()
                .map(ticketMapper::toTicketResponseDto)
                .toList();
    }
}
