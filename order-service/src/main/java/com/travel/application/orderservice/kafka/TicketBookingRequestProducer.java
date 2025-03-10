package com.travel.application.orderservice.kafka;

import com.travel.application.orderservice.dto.TicketBookingRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketBookingRequestProducer {

    private final KafkaTemplate<String, TicketBookingRequestDto> kafkaTemplate;

    /**
     * Method for sending ticket request to continue ordering process.
     *
     * @param ticketBookingRequest order's info for ordering available ticket
     */
    public void produceRequestForBookingAvailableTicket(TicketBookingRequestDto ticketBookingRequest) {

        Message<TicketBookingRequestDto> message = MessageBuilder
                .withPayload(ticketBookingRequest)
                .setHeader(KafkaHeaders.TOPIC, "order-topic")
                .setHeader(KafkaHeaders.PARTITION, 1)
                .build();

        kafkaTemplate.send(message);
    }

    public void produceCheck(TicketBookingRequestDto ticketBookingRequest) {

        Message<TicketBookingRequestDto> message = MessageBuilder
                .withPayload(ticketBookingRequest)
                .setHeader(KafkaHeaders.TOPIC, "order-topic")
                .setHeader(KafkaHeaders.PARTITION, 1)
                .build();

        kafkaTemplate.send(message);
    }
}
