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

@Component
@RequiredArgsConstructor
public class TicketBookingProducer {

    private final KafkaTemplate<String, TicketBookingDtoTemplate> kafkaTemplate;

    /**
     *
     *
     * @param ticketPaymentRequestDto
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
     *
     *
     * @param ticketBookingResponseDto
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
