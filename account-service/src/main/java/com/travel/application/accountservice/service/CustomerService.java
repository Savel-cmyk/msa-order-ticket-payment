package com.travel.application.accountservice.service;

import com.travel.application.accountservice.dto.CustomerDto;
import com.travel.application.accountservice.dto.CustomerResponseDto;
import com.travel.application.accountservice.dto.TicketPaymentRequestDto;
import com.travel.application.accountservice.dto.TicketPaymentResponseDto;
import com.travel.application.accountservice.model.Account;
import com.travel.application.accountservice.mapper.CustomerMapper;
import com.travel.application.accountservice.util.JWTAuthConverter;
import com.travel.application.accountservice.util.KeycloakAdapter;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerMapper customerMapper;
    private final AccountService accountService;
    private final JWTAuthConverter jwtAuthConverter;
    private final KeycloakAdapter keycloakAdapter;

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

        UserRepresentation userRepresentation = new UserRepresentation();
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
        UsersResource usersResource = keycloakAdapter.getUsersResource();
        Response response = usersResource.create(userRepresentation);
        if (!Objects.equals(201, response.getStatus())) {

            throw new RuntimeException("Status code " + response.getStatus());
        }

        List<UserRepresentation> userRepresentations = usersResource.searchByUsername(newUserRecord.username(), true);
        UserRepresentation userRepresentationAfterSave = userRepresentations.get(0);

        assignRole(userRepresentationAfterSave.getId(), "CUSTOMER");
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

        Map<String, Object> tokenClaims = jwtAuthConverter.retrieveJwtFromSecurityContext().getClaims();
        return customerMapper.toCustomerDto(tokenClaims);
    }

    /**
     * Service's method that deletes customer related data
     *
     * @author Savel-cmyk
     */
    @Transactional
    public void deleteCustomerByCustomer() {

        String customerUUID = jwtAuthConverter.retrieveJwtFromSecurityContext().getClaimAsString("sub");
        UserResource customer = keycloakAdapter.getUsersResource().get(customerUUID);
        UserRepresentation customerRepresentation = customer.toRepresentation();
        accountService.deleteAccountForCustomer(customerRepresentation.firstAttribute("account_id"));
        customer.remove();
    }

    /**
     * Method for handling ticket booking process
     *
     * @param ticketPaymentRequest info that is required for producing transaction
     * @author Savel-cmyk
     */
    @Transactional
    public TicketPaymentResponseDto payForTicket(TicketPaymentRequestDto ticketPaymentRequest) {

        UsersResource usersResource = keycloakAdapter.getUsersResource();
        UserRepresentation customerToPay = usersResource.get(ticketPaymentRequest.customerId()).toRepresentation();

        return accountService.payForTicket(customerToPay.firstAttribute("account_id"), ticketPaymentRequest);
    }

    /**
     * Method for role assignment to user
     *
     * @param userId user's unique identifier
     * @param roleName role's name to assign to user
     * @author Savel-cmyk
     */
    private void assignRole(String userId, String roleName) {

        UserResource user = keycloakAdapter.getUsersResource().get(userId);
        RolesResource rolesResource = keycloakAdapter.getRolesResource();
        RoleRepresentation representation = rolesResource.get(roleName).toRepresentation();
        user.roles().realmLevel().add(Collections.singletonList(representation));
    }
}
