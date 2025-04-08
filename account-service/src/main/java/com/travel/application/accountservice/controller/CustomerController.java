package com.travel.application.accountservice.controller;

import com.travel.application.accountservice.dto.CurrencyDto;
import com.travel.application.accountservice.dto.CustomerDto;
import com.travel.application.accountservice.dto.CustomerResponseDto;
import com.travel.application.accountservice.service.CustomerService;
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
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    /**
     * Endpoint for customer record creation in DB (basically user registration).
     *
     * @param customerDto requested data in DTO format to store
     * @return saved customer data in DTO format with 201 status code
     * @author Savel-cmyk
     */
    @Operation(
            description = "Endpoint for customer record creation in DB (basically user registration).",
            summary = "Store new customer.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Customer data that is requested for storing",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = CustomerDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    value = "{\n\"username\": \"demoUser\",\n" +
                                                            "\"surname\": \"Demo\",\n" +
                                                            "\"name\": \"User\",\n" +
                                                            "\"password\": \"demoUser$123\",\n" +
                                                            "\"email\": \"demoUser123@email.com\"\n}"
                                            )
                                    }
                            )
                    }
            ),
            responses = {
                    @ApiResponse(
                            description = "Customer registration was successful",
                            responseCode = "201",
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = CustomerResponseDto.class),
                                            examples = {
                                                    @ExampleObject(
                                                            value = "{\n\"id\": " +
                                                                    "\"ecf99a65-a677-45f4-b407-16ef672233fc\",\n" +
                                                                    "\"surname\": \"Demo\",\n" +
                                                                    "\"name\": \"User\",\n" +
                                                                    "\"username\": \"demoUser\",\n" +
                                                                    "\"email\": \"demoUser123@email.com\"\n}"
                                                    )
                                            }
                                    )
                            }
                    )
            }
    )
    @PostMapping("/public")
    public ResponseEntity<CustomerResponseDto> addCustomer(
            @RequestBody CustomerDto customerDto
    ) {
        CustomerResponseDto persistedCustomer = customerService.addCustomer(customerDto);
        return new ResponseEntity<>(persistedCustomer,HttpStatus.CREATED);
    }

    /**
     * Endpoint for customer's info retrieval from JWT claims
     *
     * @return customer's data that corresponds to requested unique identifier and 200 status code in case of success
     * @author Savel-cmyk
     */
    @Operation(
            description = "Endpoint for customer's info retrieval from JWT claims.",
            summary = "Retrieve customer info.",
            responses = {
                    @ApiResponse(
                            description = "Customer info retrieval was successful",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = CustomerResponseDto.class),
                                            examples = {
                                                    @ExampleObject(
                                                            value = "{\n\"id\": " +
                                                                    "\"ecf99a65-a677-45f4-b407-16ef672233fc\",\n" +
                                                                    "\"surname\": \"Demo\",\n" +
                                                                    "\"name\": \"User\",\n" +
                                                                    "\"username\": \"demoUser\",\n" +
                                                                    "\"email\": \"demoUser123@email.com\"\n}"
                                                    )
                                            }
                                    )
                            }
                    )
            }
    )
    @PreAuthorize("hasAnyRole('CUSTOMER', 'MANAGER')")
    @GetMapping("/private")
    public ResponseEntity<CustomerResponseDto> getCustomerInfo() {

        CustomerResponseDto persistedCustomer = customerService.getCustomerInfo();
        return new ResponseEntity<>(persistedCustomer, HttpStatus.OK);
    }

    /**
     * Endpoint for customer's account and entity record from security provider
     *
     * @return 204 status code
     * @author Savel-cmyk
     */
    @Operation(
            description = "Endpoint for customer's account and entity record from security provider.",
            summary = "Delete customer info.",
            responses = {
                    @ApiResponse(
                            description = "Customer info deletion was successful",
                            responseCode = "204"
                    )
            }
    )
    @PreAuthorize("hasAnyRole('CUSTOMER', 'MANAGER')")
    @DeleteMapping("/private")
    public ResponseEntity<?> deleteCustomerByCustomer() {

        customerService.deleteCustomerByCustomer();
        return ResponseEntity.noContent().build();
    }
}
