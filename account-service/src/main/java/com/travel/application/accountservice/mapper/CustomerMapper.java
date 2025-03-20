package com.travel.application.accountservice.mapper;

import com.travel.application.accountservice.dto.CustomerResponseDto;
import com.travel.application.accountservice.model.Customer;
import com.travel.application.accountservice.dto.CustomerDto;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

/**
 * @author Savel-cmyk
 * @version 0.0.1
 */
@Service
public class CustomerMapper {

    /**
     * Method for transferring data from customer DTO to newly created customer DAO
     *
     * @param customer data in DTO format
     * @return customer data in corresponding DAO format
     */
    public Customer toCustomerDao(CustomerDto customer) {
        return Customer.builder()
                .surname(customer.surname())
                .name(customer.name())
                .patronymic(customer.patronymic())
                .email(customer.email())
                .phoneNumber(customer.phoneNumber())
                .build();
    }

    /**
     * Method for transferring data from received customer DAO to newly created customer DTO
     *
     * @param persistedCustomer data in DAO format
     * @return customer data in corresponding DTO format
     */
    @Deprecated
    public CustomerDto toCustomerDto(Customer persistedCustomer) {
        return new CustomerDto(
                persistedCustomer.getUsername(),
                persistedCustomer.getSurname(),
                persistedCustomer.getName(),
                persistedCustomer.getHashedPassword(),
                persistedCustomer.getPatronymic(),
                persistedCustomer.getEmail(),
                persistedCustomer.getPhoneNumber()
        );
    }

    /**
     * Method for transferring data from received customer DAO from identity provider
     * to newly created customer DTO
     *
     * @param user persisted customer info
     * @return persisted customer's info in DTO format
     * @author Savel-cmyk
     */
    public CustomerResponseDto toCustomerDto(UserRepresentation user) {
        return new CustomerResponseDto(
                user.getId(),
                user.getLastName(),
                user.getFirstName(),
                user.getUsername(),
                user.getEmail()
        );
    }
}
