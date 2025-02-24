package com.travel.application.orderservice.kafka;

import com.travel.application.orderservice.dto.CustomerAccountExchangeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class AccountResponseConsumer {

    private final Map<String, CompletableFuture<CustomerAccountExchangeDto>> accountIdsFutures = new ConcurrentHashMap<>();

    /**
     * Method for receiving event on customer-account identifier exchange in corresponding DTO format
     *
     * @param customerAccountExchangeDto DTO for identifier exchange
     */
    @KafkaListener(topics = "account-topic", groupId = "order-group")
    public void consumeAccountId(CustomerAccountExchangeDto customerAccountExchangeDto) {

        String customerId = customerAccountExchangeDto.customerId();

        CompletableFuture<CustomerAccountExchangeDto> futureExchange = accountIdsFutures.remove(customerId);
        if (futureExchange != null) {
            futureExchange.complete(customerAccountExchangeDto);
        }
    }

    /**
     * Method for resolve access to customer-account identifier exchange result to where it needs
     *
     * @param customerId customer's unique identifier to which account's identifier corresponds
     * @return {@code CompletableFuture.class} object instance that wraps customer-account exchange object instance
     */
    public CompletableFuture<CustomerAccountExchangeDto> getAccountId(String customerId) {

        CompletableFuture<CustomerAccountExchangeDto> futureExchange = new CompletableFuture<>();
        accountIdsFutures.put(customerId, futureExchange);
        return futureExchange;
    }
}
