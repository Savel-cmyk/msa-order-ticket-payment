package com.travel.application.paymentservice.mapper;

import com.travel.application.paymentservice.dto.CurrencyDto;
import com.travel.application.paymentservice.model.Currency;
import org.springframework.stereotype.Service;

/**
 * @author Savel-cmyk
 * @version 0.0.1
 */
@Service
public class CurrencyMapper {

    /**
     * Method for mapping currency data in DTO format to DAO
     *
     * @param currency data in DTO format
     * @return currency data in DAO format
     */
    public Currency toCurrencyDao(CurrencyDto currency) {

        return Currency.builder()
                .name(currency.name())
                .exchangeRate(currency.exchangeRate())
                .description(currency.description())
                .build();
    }

    /**
     * Method for mapping currency data in DAO format to DTO format
     *
     * @param currency data in DAO format
     * @return currency data in DTO format
     */
    public CurrencyDto toCurrencyDto(Currency currency) {

        return new CurrencyDto(
                currency.getName(),
                currency.getExchangeRate(),
                currency.getDescription()
        );
    }
}
