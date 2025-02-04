package com.travel.application.orderservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@Table(name = "client_order")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String orderId;
    private String customerSNP;
    private String number;
    private String email;
    private LocalDateTime date;
    private String paymentId;
    private String ticketId;
}
