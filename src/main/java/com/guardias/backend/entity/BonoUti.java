package com.guardias.backend.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "BonosUti")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BonoUti {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;

    private String documentoLegal;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    @Column(precision = 20, scale = 2)
    private BigDecimal monto;
}
