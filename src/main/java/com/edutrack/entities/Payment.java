package com.edutrack.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // id del usuario que paga

    private Double amount; // monto del pago

    private LocalDate paymentDate; // fecha del pago

    private String status; // estado del pago (ejemplo: "PENDING", "PAID", "CANCELLED")

}
