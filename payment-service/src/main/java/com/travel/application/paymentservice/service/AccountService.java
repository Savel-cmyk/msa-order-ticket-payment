package com.travel.application.paymentservice.service;

import com.travel.application.paymentservice.dto.CustomerAccountExchangeDto;
import com.travel.application.paymentservice.mapper.AccountMapper;
import com.travel.application.paymentservice.model.Account;
import com.travel.application.paymentservice.repository.AccountRepository;
import com.travel.application.paymentservice.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Savel-cmyk
 * @version 0.0.1
 */
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;
    private final CurrencyRepository currencyRepository;

    /**
     * Method for account creation for requested customer's unique identifier
     *
     * @param customerId customer's unique identifier
     * @return customer-account exchange DTO object instance
     */
    public CustomerAccountExchangeDto addAccountForCustomerId(String customerId) {

        Account transientAccount = Account.builder()
                .customerId(UUID.fromString(customerId))
                .amount(0.0)
                .currency(currencyRepository.findByName("RUB"))
                .build();
        Account persistedAccount = accountRepository.save(transientAccount);
        return accountMapper.toCustomerAccountExchangeDto(persistedAccount);
    }
}
