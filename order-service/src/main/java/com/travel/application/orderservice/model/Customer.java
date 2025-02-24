package com.travel.application.orderservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "users",
        uniqueConstraints = @UniqueConstraint(columnNames = {"email"})
)
public class Customer {

    @Id
    private UUID id;
    private UUID accountId;
    private String surname;
    private String name;
    private String patronymic;
    private String email;
    private String phoneNumber;

}
