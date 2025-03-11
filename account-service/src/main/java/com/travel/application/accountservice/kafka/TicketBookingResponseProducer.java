package com.travel.application.accountservice.kafka;

import com.travel.application.accountservice.dto.TicketPaymentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketBookingResponseProducer {

    private final KafkaTemplate<String, TicketPaymentResponseDto> kafkaTemplate;

    /**
     * Producer's method for sending response on transaction status for ticket booking
     *
     * @param ticketPaymentResponse status of payment
     */
    public void produceResponseOnTicketBooking(TicketPaymentResponseDto ticketPaymentResponse) {

        Message<TicketPaymentResponseDto> message = MessageBuilder
                .withPayload(ticketPaymentResponse)
                .setHeader(KafkaHeaders.TOPIC, "account-topic")
                .setHeader(KafkaHeaders.PARTITION, 0)
                .build();

        kafkaTemplate.send(message);
    }
}
