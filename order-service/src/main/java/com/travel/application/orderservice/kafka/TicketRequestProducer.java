package com.travel.application.orderservice.kafka;

import com.travel.application.orderservice.dto.TicketInfoRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketRequestProducer {

    private final KafkaTemplate<String, TicketInfoRequestDto> kafkaTemplate;

    /**
     * Method for sending ticket request to message broker which can be pulled from order-topic.
     *
     * @param ticketInfoRequest
     */
    public void produceTicketRequest(TicketInfoRequestDto ticketInfoRequest) {

        Message<TicketInfoRequestDto> message = MessageBuilder
                .withPayload(ticketInfoRequest)
                .setHeader(KafkaHeaders.TOPIC, "order-topic")
                .setHeader(KafkaHeaders.PARTITION, 0)
                .build();

        kafkaTemplate.send(message);
    }
}
