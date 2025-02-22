package com.travel.application.paymentservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Double amount;
    private UUID customerId;
    @OneToOne
    @JoinColumn(name = "currency_id",referencedColumnName = "id")
    @ToString.Exclude
    private Currency currency;
}
