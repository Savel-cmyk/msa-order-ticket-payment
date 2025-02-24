package com.travel.application.paymentservice.mapper;

import com.travel.application.paymentservice.dto.CustomerAccountExchangeDto;
import com.travel.application.paymentservice.model.Account;
import org.springframework.stereotype.Service;

@Service
public class AccountMapper {

    public CustomerAccountExchangeDto toCustomerAccountExchangeDto(Account account) {

        return new CustomerAccountExchangeDto(
                String.valueOf(account.getCustomerId()),
                String.valueOf(account.getId())
        );
    }
}
