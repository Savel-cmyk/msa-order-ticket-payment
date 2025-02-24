package com.travel.application.paymentservice.service;

import com.travel.application.paymentservice.dto.CurrencyDto;
import com.travel.application.paymentservice.exception.RecordNotFoundException;
import com.travel.application.paymentservice.mapper.CurrencyMapper;
import com.travel.application.paymentservice.model.Currency;
import com.travel.application.paymentservice.repository.CurrencyRepository;
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
     * Method for retrieving data with requested id from DB
     *
     * @param currencyId currency unique identifier
     * @return persisted currency data in DTO format
     * @throws RecordNotFoundException in case of unsuccessful retrieve from DB currency info for requested id
     */
    public CurrencyDto getCurrencyById(Long currencyId) {

        Currency persistedCurrency = currencyRepository.findById(currencyId)
                .orElseThrow(() -> new RecordNotFoundException(
                                "No currency record has been found for corresponding Id",
                                Currency.class.getTypeName()
                        )
                );
        return currencyMapper.toCurrencyDto(persistedCurrency);
    }
}
