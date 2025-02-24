package com.travel.application.paymentservice.kafka;

import com.travel.application.paymentservice.dto.CustomerAccountExchangeDto;
import com.travel.application.paymentservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountRequestConsumer {

    private final AccountService accountService;
    private final AccountResponseProducer accountResponseProducer;

    /**
     * Method for account identifier request consumption and call for production of response
     *
     * @param customerId customer's unique identifier that corresponds to account identifier
     */
    @KafkaListener(topics = "customer-topic", groupId = "payment-group")
    public void consumeAccountIdRequest(String customerId) {

        CustomerAccountExchangeDto exchangeDto = accountService.addAccountForCustomerId(customerId);
        accountResponseProducer.produceAccountId(exchangeDto);
    }
}
