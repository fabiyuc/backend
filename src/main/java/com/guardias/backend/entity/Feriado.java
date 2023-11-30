package com.guardias.backend.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity(name = "Feriados")
@Data
@AllArgsConstructor
public class Feriado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.DATE)
    private LocalDate fecha;
    @Column(columnDefinition = "VARCHAR(25)")
    private String motivo;
    @Column(columnDefinition = "VARCHAR(25)")
    private String tipoFeriado; // Feriado Nacional/Provincial - Asueto administrativo - Feriado Puente
                                // Turistico
    @Column(columnDefinition = "VARCHAR(50)")
    private String descripcion;

}
