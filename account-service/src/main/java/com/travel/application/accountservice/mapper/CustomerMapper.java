package com.travel.application.accountservice.mapper;

import com.travel.application.accountservice.dto.CustomerResponseDto;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Savel-cmyk
 * @version 0.0.1
 */
@Service
public class CustomerMapper {

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

    /**
     * Method for transferring data from JWT claims to newly created customer DTO
     *
     * @param tokenClaims given JWT claims
     * @return persisted customer data
     * @author Savel-cmyk
     */
    public CustomerResponseDto toCustomerDto(Map<String, Object> tokenClaims) {

        return new CustomerResponseDto(
                (String) tokenClaims.get("sub"),
                (String) tokenClaims.get("family_name"),
                (String) tokenClaims.get("given_name"),
                (String) tokenClaims.get("preferred_username"),
                (String) tokenClaims.get("email")
        );
    }
}
