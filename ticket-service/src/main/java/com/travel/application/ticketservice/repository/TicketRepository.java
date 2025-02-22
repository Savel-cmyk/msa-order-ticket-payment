package com.travel.application.ticketservice.repository;

import com.travel.application.ticketservice.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
}
