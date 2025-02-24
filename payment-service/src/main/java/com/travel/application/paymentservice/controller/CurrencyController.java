package com.travel.application.paymentservice.controller;

import com.travel.application.paymentservice.service.CurrencyService;
import com.travel.application.paymentservice.dto.CurrencyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/currency")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    /**
     * Controller's method for adding new currency type to DB
     *
     * @param currency currency data in DTO format
     * @return response in JSON format containing persisted currency data in DTO format
     * and status code 201 in case of success
     */
    @PostMapping
    public ResponseEntity<CurrencyDto> addCurrencyType(
            @RequestBody CurrencyDto currency
    ) {
        CurrencyDto persistedCurrency = currencyService.addCurrencyType(currency);
        return new ResponseEntity<>(persistedCurrency, HttpStatus.CREATED);
    }

    /**
     * Controller's method for retrieving currency record from DB that corresponds to requested id
     *
     * @param currencyId unique identifier
     * @return response in JSON format containing persisted currency data in DTO format
     * and status code 302 in case of success
     */
    @GetMapping("/{currencyId}")
    public ResponseEntity<CurrencyDto> getCurrencyById(
            @PathVariable("currencyId") Long currencyId
    ) {
        CurrencyDto persistedCurrency = currencyService.getCurrencyById(currencyId);
        return new ResponseEntity<>(persistedCurrency, HttpStatus.OK);
    }
}
