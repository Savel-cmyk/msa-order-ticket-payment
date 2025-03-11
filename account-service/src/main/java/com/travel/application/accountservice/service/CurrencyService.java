package com.travel.application.accountservice.service;

import com.travel.application.accountservice.model.Currency;
import com.travel.application.accountservice.dto.CurrencyDto;
import com.travel.application.accountservice.exception.RecordNotFoundException;
import com.travel.application.accountservice.mapper.CurrencyMapper;
import com.travel.application.accountservice.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyMapper currencyMapper;
    private final CurrencyRepository currencyRepository;

    /**
     * Method for persisting requested data to DB
     *
     * @param currency requested data in DTO format
     * @return persisted currency data in DTO format
     */
    public CurrencyDto addCurrencyType(CurrencyDto currency) {

        Currency transientCurrency = currencyMapper.toCurrencyDao(currency);
        Currency persistedCurrency = currencyRepository.save(transientCurrency);
        return currencyMapper.toCurrencyDto(persistedCurrency);
    }

    /**
     * Method for retrieving currency data with requested id from DB
     *
     * @param currencyName currency's name
     * @return persisted currency data in DTO format
     * @throws RecordNotFoundException in case of unsuccessful retrieve from DB currency info for requested id
     */
    public CurrencyDto getCurrencyByName(String currencyName) {

        Currency persistedCurrency = currencyRepository.findByName(currencyName)
                .orElseThrow(() -> new RecordNotFoundException(
                                "No currency record has been found for corresponding Name",
                                Currency.class.getTypeName()
                        )
                );
        return currencyMapper.toCurrencyDto(persistedCurrency);
    }
}
