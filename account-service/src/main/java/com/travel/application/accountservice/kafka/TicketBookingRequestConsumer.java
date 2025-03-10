package com.travel.application.accountservice.kafka;

import com.travel.application.accountservice.dto.TicketPaymentRequestDto;
import com.travel.application.accountservice.dto.TicketPaymentResponseDto;
import com.travel.application.accountservice.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketBookingRequestConsumer {

    private final CustomerService customerService;
    private final TicketBookingResponseProducer ticketBookingResponseProducer;

    /**
     * Consumer's method for accepting ticket information from kafka broker
     *
     * @param ticketInfo information that is required for payment
     */
    @KafkaListener(
            topicPartitions = {@TopicPartition(topic = "ticket-topic", partitions = "1")},
            groupId = "account-group"
    )
    public void consumeTicketInfoForPayment(TicketPaymentRequestDto ticketInfo) {

        TicketPaymentResponseDto ticketPaymentResponse = customerService.payForTicket(ticketInfo);
        ticketBookingResponseProducer.produceResponseOnTicketBooking(ticketPaymentResponse);
    }
}
