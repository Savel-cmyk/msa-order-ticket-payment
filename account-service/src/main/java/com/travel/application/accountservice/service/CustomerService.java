package com.travel.application.accountservice.service;

import com.travel.application.accountservice.dto.CustomerDto;
import com.travel.application.accountservice.dto.CustomerResponseDto;
import com.travel.application.accountservice.dto.TicketPaymentRequestDto;
import com.travel.application.accountservice.dto.TicketPaymentResponseDto;
import com.travel.application.accountservice.exception.RecordNotFoundException;
import com.travel.application.accountservice.model.Account;
import com.travel.application.accountservice.model.Customer;
import com.travel.application.accountservice.mapper.CustomerMapper;
import com.travel.application.accountservice.repository.AccountRepository;
import com.travel.application.accountservice.repository.CustomerRepository;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;
    private final AccountService accountService;
    private final AccountRepository accountRepository;
    @Value("${app.keycloak.realm}")
    private String realm;
    private final Keycloak keycloak;

    /**
     * Service's method that maps received customer data in DTO format to DAO, saves mapped data
     * and maps saved data to DTO format to return
     *
     * @param newUserRecord customer data in DTO format
     * @return saved customer data in corresponding DTO format
     * @author Savel-cmyk
     */
    @Transactional
    public CustomerResponseDto addCustomer(CustomerDto newUserRecord) {

        UserRepresentation  userRepresentation= new UserRepresentation();
        userRepresentation.setEnabled(true);
        userRepresentation.setFirstName(newUserRecord.name());
        userRepresentation.setLastName(newUserRecord.surname());
        userRepresentation.setUsername(newUserRecord.username());
        userRepresentation.setEmail(newUserRecord.email());
        //TODO: set up email verification
        userRepresentation.setEmailVerified(true);

        Map<String, List<String>> attributes = new HashMap<>();
        Account customersAccount = accountService.addAccountForCustomer();
        attributes.put("account_id", List.of(String.valueOf(customersAccount.getId())));
        userRepresentation.setAttributes(attributes);

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setValue(newUserRecord.password());
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);

        userRepresentation.setCredentials(List.of(credentialRepresentation));

        UsersResource usersResource = keycloak.realm(realm).users();

        Response response = usersResource.create(userRepresentation);

        if(!Objects.equals(201, response.getStatus())){

            throw new RuntimeException("Status code " + response.getStatus());
        }

        List<UserRepresentation> userRepresentations = usersResource.searchByUsername(newUserRecord.username(), true);
        UserRepresentation userRepresentationAfterSave = userRepresentations.get(0);
        accountService.updateAccountForCustomer(userRepresentationAfterSave.getId(), customersAccount);

        return customerMapper.toCustomerDto(userRepresentationAfterSave);
    }

    /**
     * Service's method that retrieves customer data from JWT claims and returns it in DTO format
     *
     * @return customer's data in DTO format
     * @author Savel-cmyk
     */
    public CustomerResponseDto getCustomerInfo() {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authInfo = context.getAuthentication();
        Map<String, Object> tokenClaims = ((Jwt) authInfo.getCredentials()).getClaims();
        return customerMapper.toCustomerDto(tokenClaims);
    }

    /**
     * Method for handling ticket booking process
     *
     * @param ticketPaymentRequest info that is required for producing transaction
     */
    @Transactional
    public TicketPaymentResponseDto payForTicket(TicketPaymentRequestDto ticketPaymentRequest) {

        Customer customerToPay = customerRepository
                .findById(UUID.fromString(ticketPaymentRequest.customerId()))
                .orElseThrow(() -> new RecordNotFoundException(
                        "No customer record with requested id found",
                        Customer.class.getName()
                ));
        Account customerAccount = customerToPay.getAccount();

        TicketPaymentResponseDto ticketPaymentResponse;
        if (accountRepository.withdrawIfEnough(Double.valueOf(ticketPaymentRequest.cost()), customerAccount.getId()) > 0) {

            ticketPaymentResponse = new TicketPaymentResponseDto(
                    ticketPaymentRequest.orderId(),
                    "BOOKED",
                    ticketPaymentRequest.ticketId()
            );
        } else {
            ticketPaymentResponse = new TicketPaymentResponseDto(
                    ticketPaymentRequest.orderId(),
                    "PAYMENT_FAILED",
                    ticketPaymentRequest.ticketId()
            );
        }
        return ticketPaymentResponse;
    }
}
