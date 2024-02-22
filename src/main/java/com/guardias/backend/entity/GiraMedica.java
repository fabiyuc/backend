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
import lombok.NoArgsConstructor;

@Entity(name = "girasMedicas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiraMedica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int cantidadHoras;
    @Temporal(TemporalType.DATE)
    private LocalDate fecha;
    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;

    @Column(columnDefinition = "VARCHAR(80)")
    private String descripcion;
}
