package com.travel.application.accountservice.controller;

import com.travel.application.accountservice.dto.CustomerDto;
import com.travel.application.accountservice.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
     * Controller's method for customer record creation in DB
     *
     * @param customerDto requested data in DTO format to store
     * @return saved customer data in DTO format with 201 status code
     */
    @PreAuthorize("hasRole('admin')")
    @PostMapping
    public ResponseEntity<CustomerDto> addCustomer(
            @RequestBody CustomerDto customerDto
    ) {
        CustomerDto persistedCustomer = customerService.addCustomer(customerDto);
        return new ResponseEntity<>(persistedCustomer,HttpStatus.CREATED);
    }

    /**
     * Controller's method for customer's info retrieval from DB for corresponding id
     *
     * @param customerId customer's unique identifier to retrieve from DB
     * @return customer's data that corresponds to requested unique identifier and 200 status code in case of success
     */
    @PreAuthorize("hasRole('customer')")
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDto> getCustomerById(
            @PathVariable("customerId") String customerId
    ) {
        CustomerDto persistedCustomer = customerService.getCustomerById(customerId);
        return new ResponseEntity<>(persistedCustomer, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('admin')")
    @GetMapping("/ping")
    public String ping() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return "Scopes: " + authentication.getAuthorities();
    }
}
