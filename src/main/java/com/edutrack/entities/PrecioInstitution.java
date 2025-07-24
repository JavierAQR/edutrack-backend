package com.edutrack.entities;

import java.math.BigDecimal;
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
@Table(name = "precios_institutions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrecioInstitution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "institution_id", nullable = false)
    private Institution institution;

    @Column(nullable = false)
    private String tipo; // "matricula" o "cuota"

    @Column(nullable = false)
    private BigDecimal monto;

    @Column(nullable = false)
    private int anio;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}
