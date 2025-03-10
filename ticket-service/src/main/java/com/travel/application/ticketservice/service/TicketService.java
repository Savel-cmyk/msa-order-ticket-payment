package com.travel.application.ticketservice.service;

import com.travel.application.ticketservice.dto.*;
import com.travel.application.ticketservice.dto.impl.TicketBookingRequestDto;
import com.travel.application.ticketservice.dto.impl.TicketBookingResponseDto;
import com.travel.application.ticketservice.exception.RecordNotFoundException;
import com.travel.application.ticketservice.mapper.TicketMapper;
import com.travel.application.ticketservice.model.Ticket;
import com.travel.application.ticketservice.model.TicketStatus;
import com.travel.application.ticketservice.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

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
     *
     * @param ticketInfoRequest
     * @return ticket response in DTO format with adjusted field values
     */
    public TicketResponseDto getTicketInfo(TicketInfoRequestDto ticketInfoRequest) {

        Ticket ticket = ticketRepository.findById(UUID.fromString(ticketInfoRequest.ticketId()))
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
     *
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
     *
     * @return list of all tickets in DTO format that DB contains
     */
    public List<TicketResponseDto> getAllTickets() {

        List<Ticket> tickets = ticketRepository.findAll();
        return tickets.stream()
                .map(ticketMapper::toTicketResponseDto)
                .toList();
    }

    /**
     *
     *
     * @param ticketBookingRequest
     * @return
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public TicketBookingDtoTemplate bookAvailableTicket(TicketBookingRequestDto ticketBookingRequest) {

        Ticket availableTicket = ticketRepository.findFirstByStatus(TicketStatus.AVAILABLE);
        if (availableTicket == null) {
            return ticketMapper.toTicketBookingDto(ticketBookingRequest, "NO_AVAILABLE_TICKETS", null);
        }
        availableTicket.setStatus(TicketStatus.PROCESSING);
        Ticket processingTicket = ticketRepository.save(availableTicket);
        return ticketMapper.toTicketBookingDto(
                ticketBookingRequest,
                processingTicket.getCost(),
                String.valueOf(processingTicket.getTicketId())
        );
    }

    /**
     *
     *
     * @param ticketPaymentResponse
     * @return
     */
    @Transactional
    public TicketBookingResponseDto cancelBooking(TicketBookingResponseDto ticketPaymentResponse) {

        Ticket processingTicket = ticketRepository.findById(UUID.fromString(ticketPaymentResponse.ticketId()))
                .orElseThrow(() -> new RecordNotFoundException(
                        "Couldn't find ticket record with such id in database.",
                        Ticket.class.getTypeName()
                ));

        // Releasing from booking
        processingTicket.setStatus(TicketStatus.AVAILABLE);
        ticketRepository.save(processingTicket);

        return ticketMapper.toTicketBookingDto(
                ticketPaymentResponse,
                "CANCELED",
                String.valueOf(processingTicket.getTicketId())
        );
    }

    /**
     *
     *
     * @param ticketPaymentResponse
     * @return
     */
    @Transactional
    public TicketBookingResponseDto confirmBooking(TicketBookingResponseDto ticketPaymentResponse) {

        Ticket processingTicket = ticketRepository.findById(UUID.fromString(ticketPaymentResponse.ticketId()))
                .orElseThrow(() -> new RecordNotFoundException(
                        "Couldn't find ticket record with such id in database.",
                        Ticket.class.getTypeName()
                ));

        // Booking ticket after successful payment
        processingTicket.setStatus(TicketStatus.BOOKED);
        ticketRepository.save(processingTicket);

        return ticketMapper.toTicketBookingDto(
                ticketPaymentResponse,
                "CONFIRMED",
                String.valueOf(processingTicket.getTicketId())
        );
    }
}
