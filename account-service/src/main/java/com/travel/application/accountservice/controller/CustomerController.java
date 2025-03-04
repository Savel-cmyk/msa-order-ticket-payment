package com.travel.application.accountservice.controller;

import com.travel.application.accountservice.dto.CustomerDto;
import com.travel.application.accountservice.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerDto> addCustomer(
            @RequestBody CustomerDto customerDto
    ) {
        CustomerDto persistedCustomer = customerService.addCustomer(customerDto);
        return new ResponseEntity<>(persistedCustomer,HttpStatus.CREATED);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDto> getCustomerById(
            @PathVariable("customerId") String customerId
    ) {
        CustomerDto persistedCustomer = customerService.getCustomerById(customerId);
        return new ResponseEntity<>(persistedCustomer, HttpStatus.OK);
    }
}
