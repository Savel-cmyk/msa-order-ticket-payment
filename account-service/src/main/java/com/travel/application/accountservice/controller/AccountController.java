package com.travel.application.accountservice.controller;

import com.travel.application.accountservice.dto.AccountPutMoneyDto;
import com.travel.application.accountservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PreAuthorize("hasAnyRole('CUSTOMER', 'MANAGER')")
    @PutMapping("/private")
    public ResponseEntity<?> putMoneyOnAccount(
            @RequestBody AccountPutMoneyDto accountPutMoneyDto
    ) {
        accountService.putMoneyOnAccount(accountPutMoneyDto);
        return ResponseEntity.ok().build();
    }
}
