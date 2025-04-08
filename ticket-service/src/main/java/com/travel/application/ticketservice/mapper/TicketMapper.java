package com.travel.application.ticketservice.mapper;

import com.travel.application.ticketservice.dto.TicketBookingDtoTemplate;
import com.travel.application.ticketservice.dto.impl.TicketBookingRequestDto;
import com.travel.application.ticketservice.dto.impl.TicketBookingResponseDto;
import com.travel.application.ticketservice.dto.TicketRequestDto;
import com.travel.application.ticketservice.dto.TicketResponseDto;
import com.travel.application.ticketservice.dto.impl.TicketPaymentRequestDto;
import com.travel.application.ticketservice.model.Ticket;
import com.travel.application.ticketservice.model.TicketStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Savel-cmyk
 * @version 0.0.1
 */
@Service
public class TicketMapper {

    /**
     * Method for ticket conversion to DTO format.
     *
     * @param ticket
     * @return ticket data in DTO format
     * @author Savel-cmyk
     */
    // TODO: Here may occur exceptions about wrong type conversion (UUID to String and Double to String)
    public TicketResponseDto toTicketResponseDto(Ticket ticket) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        String dateInAppropriateFormat = ticket.getDate().format(formatter);

        return new TicketResponseDto(
                String.valueOf(ticket.getTicketId()),
                //TODO: need to convert cost to an appropriate format with two zeroes after point
                ticket.getCost().toString(),
                dateInAppropriateFormat
        );
    }

    /**
     * Method for DTO format conversion with ticket data to entity class format.
     *
     * @param ticketRequest
     * @return ticket data as an entity object
     * @author Savel-cmyk
     */
    public Ticket toTicket(TicketRequestDto ticketRequest) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        LocalDateTime localDateTime = LocalDateTime.parse(ticketRequest.date(), formatter);

        return Ticket.builder()
                .attractionId(Long.parseLong(ticketRequest.attractionId()))
                .cost(Double.parseDouble(ticketRequest.cost()))
                .status(TicketStatus.AVAILABLE)
                .date(localDateTime)
                .build();
    }

    /**
     * Method for {@code TicketBookingDtoTemplate.class} implementations to
     * {@code TicketBookingResponseDto.class} conversion. This method also
     * appears to be overloading of similar mapping method.
     *
     * @param ticketBookingDto {@code TicketBookingDtoTemplate.class} implementation
     * @param ticketStatus status of ticket booking
     * @param ticketId ticket's unique identifier
     * @return ticket booking response data in DTO format
     * @author Savel-cmyk
     */
    public TicketBookingResponseDto toTicketBookingDto(
            TicketBookingDtoTemplate ticketBookingDto,
            String ticketStatus,
            String ticketId
    ) {
        return new TicketBookingResponseDto(
                ticketBookingDto.orderId(),
                ticketStatus,
                ticketId
        );
    }

    /**
     * Method for {@code TicketBookingRequestDto.class} to {@code TicketPaymentRequestDto.class}
     * conversion. This method also appears to be overloading of similar mapping method.
     *
     * @param ticketBookingRequest ticket booking info required for payment
     * @param ticketCost ticket cost
     * @param ticketId ticket's unique identifier
     * @return ticket info in DTO format that is required for payment
     * @author Savel-cmyk
     */
    public TicketPaymentRequestDto toTicketBookingDto(
            TicketBookingRequestDto ticketBookingRequest,
            Double ticketCost,
            String ticketId
    ) {
        return new TicketPaymentRequestDto(
                ticketBookingRequest.orderId(),
                ticketCost.toString(),
                ticketBookingRequest.customerId(),
                ticketId
        );
    }
}
