package com.edutrack.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pagos_student")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private StudentProfile student;

    @ManyToOne
    @JoinColumn(name = "precio_institution_id", nullable = false)
    private PrecioInstitution precioInstitution;

    private LocalDate fechaPago;

    @Column(nullable = false)
    private String estadoPago; // "pendiente" o "pagado"
}
