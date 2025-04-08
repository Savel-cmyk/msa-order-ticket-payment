package com.travel.application.ticketservice.kafka;

import com.travel.application.ticketservice.dto.impl.TicketBookingResponseDto;
import com.travel.application.ticketservice.dto.TicketBookingDtoTemplate;
import com.travel.application.ticketservice.dto.impl.TicketPaymentRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * @author Savel-cmyk
 * @version 0.0.1
 */
@Component
@RequiredArgsConstructor
public class TicketBookingProducer {

    private final KafkaTemplate<String, TicketBookingDtoTemplate> kafkaTemplate;

    /**
     * Method for sending message to broker for customer to pay.
     *
     * @param ticketPaymentRequestDto ticket info that is required for payment
     * @author Savel-cmyk
     */
    public void produceTicketInfoForCustomerPayment(TicketPaymentRequestDto ticketPaymentRequestDto) {

        Message<TicketPaymentRequestDto> message = MessageBuilder
                .withPayload(ticketPaymentRequestDto)
                .setHeader(KafkaHeaders.TOPIC, "ticket-topic")
                .setHeader(KafkaHeaders.PARTITION, 1)
                .build();

        kafkaTemplate.send(message);
    }

    /**
     * Method for sending message to broker on ticket booking status.
     *
     * @param ticketBookingResponseDto ticket info on booking status
     * @author Savel-cmyk
     */
    public void produceResponseOnPaymentStatus(TicketBookingResponseDto ticketBookingResponseDto) {

        Message<TicketBookingResponseDto> message = MessageBuilder
                .withPayload(ticketBookingResponseDto)
                .setHeader(KafkaHeaders.TOPIC, "ticket-topic")
                .setHeader(KafkaHeaders.PARTITION, 2)
                .build();

        kafkaTemplate.send(message);
    }
}
