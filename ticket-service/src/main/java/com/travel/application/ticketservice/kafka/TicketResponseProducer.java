package com.travel.application.ticketservice.kafka;

import com.travel.application.ticketservice.dto.TicketResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketResponseProducer {

    private final KafkaTemplate<String, TicketResponseDto> kafkaTemplate;

    /**
     * Method for sending message to broker on requested ticket data to ticket-service topic.
     *
     * @param ticketResponseDto
     */
    public void produceTicketResponse(TicketResponseDto ticketResponseDto) {

        Message<TicketResponseDto> message = MessageBuilder
                .withPayload(ticketResponseDto)
                .setHeader(KafkaHeaders.TOPIC, "ticket-topic")
                .setHeader(KafkaHeaders.PARTITION, 0)
                .build();

        kafkaTemplate.send(message);
    }
}
