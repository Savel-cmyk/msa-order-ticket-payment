package com.travel.application.accountservice.util;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Savel-cmyk
 * @version 0.0.1
 */
@Component
@RequiredArgsConstructor
public class KeycloakAdapter {

    @Value("${app.keycloak.realm}")
    private String realm;
    private final Keycloak keycloak;

    /**
     * Method for fetching user entity from security provider storage
     *
     * @return lazy(?) fetched users from security provider storage
     * @author Savel-cmyk
     */
    public UsersResource getUsersResource() {

        return keycloak.realm(realm).users();
    }

    /**
     * Method for fetching user's roles from security provider storage
     *
     * @return lazy(?) fetched roles from security provider storage
     * @author Savel-cmyk
     */
    public RolesResource getRolesResource() {

        return keycloak.realm(realm).roles();
    }
}
