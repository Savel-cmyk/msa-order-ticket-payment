package com.travel.application.orderservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

/**
 * @param surname
 * @param name
 * @param patronymic
 * @param email <i><b>test cases:</b></i><br/>
 * <i>0r.4cu.5t@gmail.mail.com - <b>true</b>,</i><br/>
 * <i>gu@rubic.ru - <b>true</b>,</i><br/>
 * <i>13@out.1 - <b>false</b></i>
 * @param phoneNumber
 * @author Savel-cmyk
 * @version 0.0.1
 */
public record CustomerDto(

        @NotBlank
        String surname,
        @NotBlank
        String name,
        @NotEmpty
        String patronymic,
        @Pattern(
                regexp = "^([0-9]+[a-zA-Z]+|[a-zA-Z]+[a-zA-Z0-9]*)([.]([0-9]+[a-zA-Z]+|[a-zA-Z]+[a-zA-Z0-9]*)){0,2}" +
                        "@[a-zA-Z]+([.][a-zA-Z]+)?[.][a-zA-Z]{2,}$",
                message = "Email format is not valid"
        )
        @NotEmpty
        String email,
        @Pattern(
                regexp = "^[0-9]{7,10}$",
                message = "Phone number format is not valid"
        )
        @NotEmpty
        String phoneNumber
) {
}
