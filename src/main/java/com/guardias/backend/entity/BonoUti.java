package com.guardias.backend.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
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

    @OneToOne(mappedBy = "bonoUti", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler",
            "bonoUti" })
    private ValorGuardiaBase valorGuardia;
}
