package com.guardias.backend.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor

public class Cargo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String nombre;
    private String descripcion;
    private String nroresolucion;
    private String nrodecreto;
    private LocalDate fecharesolucion;
    private LocalDate fechainicio;
    private LocalDate fechafinal;

    public Cargo(Long id, String nombre, String descripcion, String nroresolucion, String nrodecreto,
            LocalDate fecharesolucion, LocalDate fechainicio, LocalDate fechafinal) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.nroresolucion = nroresolucion;
        this.nrodecreto = nrodecreto;
        this.fecharesolucion = fecharesolucion;
        this.fechainicio = fechainicio;
        this.fechafinal = fechafinal;
    }

}
