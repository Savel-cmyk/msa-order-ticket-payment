package com.travel.application.orderservice.controller;

import com.travel.application.orderservice.dto.CustomerDto;
import com.travel.application.orderservice.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Savel-cmyk
 * @version 0.0.1
 */
@RestController
@RequestMapping("/api/v1/orders/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    /**
     * Controller's method for creating record in DB from received customer data
     * @param customer data in DTO format
     * @return JSON that contains persisted customer data in DTO format and 201 status code in case of success
     */
    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(
            @RequestBody @Valid CustomerDto customer
    ) {
        CustomerDto persistedCustomer = customerService.createCustomer(customer);
        return new ResponseEntity<>(persistedCustomer, HttpStatus.CREATED);
    }

    /**
     * Controller's method for retrieving customer data that corresponds to requested id value
     * @param customerId customer unique identifier
     * @return JSON that contains requested customer data that corresponds to given id and 302 status
     * code in case of success
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDto> getCustomerById(
            @PathVariable String customerId
    ) {
        CustomerDto requestedCustomer = customerService.getCustomerById(customerId);
        return new ResponseEntity<>(requestedCustomer, HttpStatus.FOUND);
    }
}
