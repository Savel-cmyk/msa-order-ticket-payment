package com.travel.application.accountservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Version;

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
    @ManyToOne
    @JoinColumn(name = "currency_id",referencedColumnName = "id")
    @ToString.Exclude
    private Currency currency;
    private UUID customerId;
    @Version
    private Long version;
}
