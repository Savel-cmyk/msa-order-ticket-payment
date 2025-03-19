package com.travel.application.accountservice.controller;

import com.travel.application.accountservice.service.CurrencyService;
import com.travel.application.accountservice.dto.CurrencyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('admin')")
    @PostMapping
    public ResponseEntity<CurrencyDto> addCurrencyType(
            @RequestBody CurrencyDto currency
    ) {
        CurrencyDto persistedCurrency = currencyService.addCurrencyType(currency);
        return new ResponseEntity<>(persistedCurrency, HttpStatus.CREATED);
    }

    /**
     * Controller's method for retrieving currency record from DB that corresponds to requested name value
     *
     * @param currencyName currency's name
     * @return response in JSON format containing persisted currency data in DTO format
     * and status code 302 in case of success
     */
    @PreAuthorize("hasRole('customer')")
    @GetMapping("/{currencyName}")
    public ResponseEntity<CurrencyDto> getCurrencyByName(
            @PathVariable("currencyName") String currencyName
    ) {
        CurrencyDto persistedCurrency = currencyService.getCurrencyByName(currencyName);
        return new ResponseEntity<>(persistedCurrency, HttpStatus.OK);
    }
}
