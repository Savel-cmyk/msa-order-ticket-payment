package com.travel.application.accountservice.mapper;

import com.travel.application.accountservice.dto.AccountDto;
import com.travel.application.accountservice.model.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountMapper {

    private final CurrencyMapper currencyMapper;

    public AccountDto toAccountDto(Account accountDao) {

        return new AccountDto(
                accountDao.getAmount(),
                currencyMapper.toCurrencyDto(accountDao.getCurrency())
        );
    }
}
