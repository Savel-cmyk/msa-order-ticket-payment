package com.travel.application.ticketservice.mapper;

import com.travel.application.ticketservice.dto.TicketRequestDto;
import com.travel.application.ticketservice.dto.TicketResponseDto;
import com.travel.application.ticketservice.model.Ticket;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class TicketMapper {

    /**
     * Method for ticket conversion to DTO format.
     * @param ticket
     * @return ticket data in DTO format
     */
    // TODO: Here may occur exceptions about wrong type conversion (UUID to String and Double to String)
    public TicketResponseDto toTicketResponseDto(Ticket ticket) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        String dateInAppropriateFormat = ticket.getDate().format(formatter);

        return new TicketResponseDto(
                ticket.getTicketId(),
                //TODO: need to convert cost to an appropriate format with two zeroes after point
                ticket.getCost().toString(),
                dateInAppropriateFormat
        );
    }

    /**
     * Method for DTO format conversion with ticket data to entity class format.
     * @param ticketRequest
     * @return ticket data as an entity object
     */
    public Ticket toTicket(TicketRequestDto ticketRequest) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        LocalDateTime localDateTime = LocalDateTime.parse(ticketRequest.date(), formatter);

        return Ticket.builder()
                .attractionId(Long.parseLong(ticketRequest.attractionId()))
                .cost(Double.parseDouble(ticketRequest.cost()))
                .date(localDateTime)
                .build();
    }
}
