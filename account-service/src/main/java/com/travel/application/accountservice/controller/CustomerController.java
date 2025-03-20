package com.travel.application.accountservice.controller;

import com.travel.application.accountservice.dto.CustomerDto;
import com.travel.application.accountservice.dto.CustomerResponseDto;
import com.travel.application.accountservice.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

/**
 * @author Savel-cmyk
 * @version 0.0.1
 */
@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    /**
     * Controller's method for customer record creation in DB (basically user registration)
     *
     * @param customerDto requested data in DTO format to store
     * @return saved customer data in DTO format with 201 status code
     * @author Savel-cmyk
     */
    @PostMapping("/public")
    public ResponseEntity<CustomerResponseDto> addCustomer(
            @RequestBody CustomerDto customerDto
    ) {
        CustomerResponseDto persistedCustomer = customerService.addCustomer(customerDto);
        return new ResponseEntity<>(persistedCustomer,HttpStatus.CREATED);
    }

    /**
     * Controller's method for customer's info retrieval from JWT claims
     *
     * @return customer's data that corresponds to requested unique identifier and 200 status code in case of success
     * @author Savel-cmyk
     */
    @PreAuthorize("hasAnyRole('customer', 'admin')")
    @GetMapping("/private")
    public ResponseEntity<CustomerResponseDto> getCustomerInfo() {
        CustomerResponseDto persistedCustomer = customerService.getCustomerInfo();
        return new ResponseEntity<>(persistedCustomer, HttpStatus.OK);
    }

//    @PreAuthorize("hasRole('admin')")
    @GetMapping("/public/ping")
    public String ping() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return "Details: " + authentication.getDetails() +
                "Principal: " + ((Jwt) authentication.getPrincipal()).getClaims() +
                "Scopes: " + authentication.getAuthorities() +
                "Credentials: " + ((Jwt) authentication.getCredentials()).getClaims();
    }
}
