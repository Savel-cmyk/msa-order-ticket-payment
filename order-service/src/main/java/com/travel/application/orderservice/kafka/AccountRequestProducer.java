package com.travel.application.orderservice.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountRequestProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Method for customer and account identifiers exchange request to message broker
     * which can be pulled from customer-topic.
     * @param customerId customer unique identifier
     */
    public void produceAccountRequest(String customerId) {

        kafkaTemplate.send("customer-topic", customerId);
    }
}
