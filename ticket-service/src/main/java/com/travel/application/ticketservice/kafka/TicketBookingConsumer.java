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
     *
     *
     * @param ticketBookingRequest
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
     *
     *
     * @param ticketPaymentResponse
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

//    @KafkaListener(
//            topicPartitions = {@TopicPartition(topic = "order-topic", partitions = {"1"})},
//            groupId = "ticket-group"
//    )
//    public void consumeCheckForPartitionsWork(TicketBookingRequestDto ticketBookingRequest) {
//
//        System.out.println(ticketBookingRequest);
//    }
}
