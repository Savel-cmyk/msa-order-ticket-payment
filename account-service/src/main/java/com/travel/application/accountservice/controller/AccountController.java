package com.travel.application.accountservice.controller;

import com.travel.application.accountservice.dto.AccountPutMoneyDto;
import com.travel.application.accountservice.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Savel-cmyk
 * @version 0.0.1
 */
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    /**
     * Endpoint for increasing amount of money on authenticated user account
     *
     * @param accountPutMoneyDto info on change to put
     * @return 200 status in case of success
     * @author Savel-cmyk
     */
    @Operation(
            description = "Endpoint for increasing amount of money on authenticated user account.",
            summary = "Add money to authenticated user.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Amount of money to put",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = AccountPutMoneyDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = "{\n\"change\": \"500.00\"\n}"
                                            )
                                    }
                            )
                    }
            ),
            responses = {
                    @ApiResponse(
                            description = "Account update was successful",
                            responseCode = "200"
                    )
            }
    )
    @PreAuthorize("hasAnyRole('CUSTOMER', 'MANAGER')")
    @PutMapping("/private")
    public ResponseEntity<?> putMoneyOnAccount(
            @RequestBody AccountPutMoneyDto accountPutMoneyDto
    ) {
        accountService.putMoneyOnAccount(accountPutMoneyDto);
        return ResponseEntity.ok().build();
    }
}
