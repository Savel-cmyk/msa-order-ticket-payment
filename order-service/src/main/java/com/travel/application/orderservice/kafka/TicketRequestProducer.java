package com.travel.application.orderservice.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketRequestProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Method for sending ticket request to message broker which can be pulled from order-topic.
     * @param ticketId
     */
    public void produceTicketRequest(String ticketId) {

        kafkaTemplate.send("order-topic", ticketId);
    }
}
