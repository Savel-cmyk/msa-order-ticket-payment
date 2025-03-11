package com.travel.application.accountservice.service;

import com.travel.application.accountservice.exception.RecordNotFoundException;
import com.travel.application.accountservice.model.Account;
import com.travel.application.accountservice.model.Currency;
import com.travel.application.accountservice.repository.AccountRepository;
import com.travel.application.accountservice.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Savel-cmyk
 * @version 0.0.1
 */
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final CurrencyRepository currencyRepository;

    /**
     * Method for account creation for requested customer's unique identifier
     *
     * @return customer-account exchange DTO object instance
     */
    public Account addAccountForCustomer() {

        Account customerAccount = Account.builder()
                .amount(0.0)
                .currency(
                        currencyRepository.findByName("RUB")
                                .orElseThrow(() -> new RecordNotFoundException(
                                        "No currency record has been found for corresponding Name",
                                        Currency.class.getTypeName()
                                ))
                )
                .build();
        return accountRepository.save(customerAccount);
    }
}
