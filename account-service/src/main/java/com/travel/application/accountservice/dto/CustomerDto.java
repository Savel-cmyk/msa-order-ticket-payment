package com.travel.application.accountservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

/**
 * @param username
 * @param surname
 * @param name
 * @param password
 * @param email <i><b>test cases:</b></i><br/>
 * <i>0r.4cu.5t@gmail.mail.com - <b>true</b>,</i><br/>
 * <i>gu@rubic.ru - <b>true</b>,</i><br/>
 * <i>13@out.1 - <b>false</b></i>
 * @author Savel-cmyk
 * @version 0.0.1
 */
public record CustomerDto(

        @NotBlank
        String username,
        @NotBlank
        String surname,
        @NotBlank
        String name,
        @NotBlank
        String password,
        @Pattern(
                regexp = "^([0-9]+[a-zA-Z]+|[a-zA-Z]+[a-zA-Z0-9]*)([.]([0-9]+[a-zA-Z]+|[a-zA-Z]+[a-zA-Z0-9]*)){0,2}" +
                        "@[a-zA-Z]+([.][a-zA-Z]+)?[.][a-zA-Z]{2,}$",
                message = "Email format is not valid"
        )
        @NotEmpty
        String email
) {
}
