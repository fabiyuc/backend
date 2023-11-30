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

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cargo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "VARCHAR(30)")
    private String nombre;
    @Column(columnDefinition = "VARCHAR(80)")
    private String descripcion;
    @Column(columnDefinition = "VARCHAR(10)")
    private String nroresolucion;
    @Column(columnDefinition = "VARCHAR(10)")
    private String nrodecreto;
    @Temporal(TemporalType.DATE)
    private LocalDate fecharesolucion;
    @Temporal(TemporalType.DATE)
    private LocalDate fechainicio;
    @Temporal(TemporalType.DATE)
    private LocalDate fechafinal;

}
