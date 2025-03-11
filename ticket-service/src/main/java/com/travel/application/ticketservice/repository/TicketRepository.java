package com.travel.application.ticketservice.repository;

import com.travel.application.ticketservice.model.Ticket;
import com.travel.application.ticketservice.model.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    Ticket findFirstByStatus(TicketStatus ticketStatus);
}
