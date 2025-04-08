package com.travel.application.ticketservice.kafka;

import com.travel.application.ticketservice.dto.TicketBookingDtoTemplate;
import com.travel.application.ticketservice.dto.impl.TicketBookingRequestDto;
import com.travel.application.ticketservice.dto.impl.TicketBookingResponseDto;
import com.travel.application.ticketservice.dto.impl.TicketPaymentRequestDto;
import com.travel.application.ticketservice.model.TicketStatus;
import com.travel.application.ticketservice.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketBookingConsumer {

    private final TicketService ticketService;
    private final TicketBookingProducer ticketBookingProducer;

    /**
     * Listener method for ticket request for ordering consumption and calling
     * for respond producer method depending on booking available method result:
     * if booking status is not null, there is no available ticket for booking,
     * if booking status is null, request for payment is produced.
     *
     * @param ticketBookingRequest ticket booking required info
     */
    @KafkaListener(
            topicPartitions = {@TopicPartition(topic = "order-topic", partitions = {"1"})},
            groupId = "ticket-group"
    )
    public void consumeTicketRequestForOrdering(TicketBookingRequestDto ticketBookingRequest) {

        TicketBookingDtoTemplate ticketBookingDto = ticketService.bookAvailableTicket(ticketBookingRequest);
        if (ticketBookingDto.bookingStatus() != null) {
            ticketBookingProducer.produceResponseOnPaymentStatus((TicketBookingResponseDto) ticketBookingDto);
        } else {
            ticketBookingProducer.produceTicketInfoForCustomerPayment((TicketPaymentRequestDto) ticketBookingDto);
        }
    }

    /**
     * Listener method for booking or releasing from hold chosen available ticket
     * before payment depending on payment status: if payment status is BOOKED,
     * then payment was successful and ticket should be permanently booked,
     * if payment status is PAYMENT_FAILED, ticket should be released and for every
     * case response for order service should be produced considering booking status.
     *
     * @param ticketPaymentResponse ticket payment received info that is required for described actions
     */
    @KafkaListener(
            topicPartitions = {@TopicPartition(topic = "account-topic", partitions = {"0"})},
            groupId = "ticket-group"
    )
    public void consumeResponseOnPaymentStatus(TicketBookingResponseDto ticketPaymentResponse) {

        TicketBookingResponseDto ticketBookingResponse;
        if (TicketStatus.valueOf(ticketPaymentResponse.bookingStatus()) == TicketStatus.BOOKED) {
            ticketBookingResponse = ticketService.confirmBooking(ticketPaymentResponse);
        } else {
            ticketBookingResponse = ticketService.cancelBooking(ticketPaymentResponse);
        }

        ticketBookingProducer.produceResponseOnPaymentStatus(ticketBookingResponse);
    }
}
