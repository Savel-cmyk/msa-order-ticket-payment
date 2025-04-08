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

/**
 * @author Savel-cmyk
 * @version 0.0.1
 */
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
     * @author Savel-cmyk
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
     * @author Savel-cmyk
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
     * @author Savel-cmyk
     */
    public List<TicketResponseDto> getAllTickets() {

        List<Ticket> tickets = ticketRepository.findAll();
        return tickets.stream()
                .map(ticketMapper::toTicketResponseDto)
                .toList();
    }

    /**
     * Method for booking one available ticket, depending on if there are any available:
     * if there is no ticket, event on order canceling with NO_AVAILABLE_TICKETS status is returned,
     * if there is at least one ticket, event on payment request is returned.
     *
     * @param ticketBookingRequest ticket info that is required for booking steps
     * @return event on order canceling with NO_AVAILABLE_TICKETS status or on payment request
     * @author Savel-cmyk
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
     * Method for canceling ticket booking and releasing ticket that is on hold for payment.
     *
     * @param ticketPaymentResponse ticket info that is required for canceling and producing canceling event
     * @return event on order canceling due to interrupted payment
     * @author Savel-cmyk
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
     * Method for confirming ticket booking and changing ticket status that is on
     * hold for payment to BOOKED.
     *
     * @param ticketPaymentResponse ticket info that is required for booking and producing confirmation event
     * @return event on order confirmation due to successful payment
     * @author Savel-cmyk
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
