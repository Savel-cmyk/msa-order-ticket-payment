package com.travel.application.paymentservice.kafka;

import com.travel.application.paymentservice.dto.CustomerAccountExchangeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountResponseProducer {

    private final KafkaTemplate<String, CustomerAccountExchangeDto> kafkaTemplate;

    /**
     * Method response production to customer that requested account's id
     *
     * @param exchangeDto DTO for customer-account unique identifier's exchange
     */
    public void produceAccountId(CustomerAccountExchangeDto exchangeDto) {

        Message<CustomerAccountExchangeDto> message = MessageBuilder
                .withPayload(exchangeDto)
                .setHeader(KafkaHeaders.TOPIC, "account-topic")
                .build();

        kafkaTemplate.send(message);
    }
}
