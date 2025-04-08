package com.travel.application.accountservice.controller;

import com.travel.application.accountservice.service.CurrencyService;
import com.travel.application.accountservice.dto.CurrencyDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Savel-cmyk
 * @version 0.0.1
 */
@RestController
@RequestMapping("/api/v1/currency")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    /**
     * Endpoint for adding new currency type to DB.
     *
     * @param currency currency data in DTO format
     * @return response in JSON format containing persisted currency data in DTO format
     * and status code 201 in case of success
     * @author Savel-cmyk
     */
    @Operation(
            description = "Endpoint for adding new currency type to DB.",
            summary = "Add new currency to storage.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Currency data that is requested for storing",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = CurrencyDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = "{\n\"name\": \"RUB\",\n" +
                                                            "\"exchangeRate\": \"1.0\",\n" +
                                                            "\"description\": \"Russian ruble currency\"\n}"
                                            )
                                    }
                            )
                    }
            ),
            responses = {
                    @ApiResponse(
                            description = "Currency creation was successful",
                            responseCode = "201",
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = CurrencyDto.class),
                                            examples = {
                                                    @ExampleObject(
                                                            value = "{\n\"name\": \"RUB\",\n" +
                                                                    "\"exchangeRate\": \"1.0\",\n" +
                                                                    "\"description\": \"Russian ruble currency\"\n}"
                                                    )
                                            }
                                    )
                            }
                    )
            }
    )
    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/private")
    public ResponseEntity<CurrencyDto> addCurrencyType(
            @RequestBody CurrencyDto currency
    ) {
        CurrencyDto persistedCurrency = currencyService.addCurrencyType(currency);
        return new ResponseEntity<>(persistedCurrency, HttpStatus.CREATED);
    }

    /**
     * Endpoint for retrieving currency record from DB that corresponds to requested name value
     *
     * @param currencyName currency's name
     * @return response in JSON format containing persisted currency data in DTO format
     * and status code 302 in case of success
     * @author Savel-cmyk
     */
    @Operation(
            description = "Endpoint for retrieving currency record from DB that corresponds to requested name value.",
            summary = "Retrieval of currency info by name.",
            parameters = {
                    @Parameter(
                            name = "currencyName",
                            description = "currency name to retrieve by",
                            example = "RUB"
                    )
            },
            responses = {
                    @ApiResponse(
                            description = "Currency info retrieval was successful",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = CurrencyDto.class),
                                            examples = {
                                                    @ExampleObject(
                                                            value = "{\n\"name\": \"RUB\",\n" +
                                                                    "\"exchangeRate\": \"1.0\",\n" +
                                                                    "\"description\": \"Russian ruble currency\"\n}"
                                                    )
                                            }
                                    )
                            }
                    )
            }
    )
    @GetMapping("/public/{currencyName}")
    public ResponseEntity<CurrencyDto> getCurrencyByName(
            @PathVariable("currencyName") String currencyName
    ) {
        CurrencyDto persistedCurrency = currencyService.getCurrencyByName(currencyName);
        return new ResponseEntity<>(persistedCurrency, HttpStatus.OK);
    }
}
