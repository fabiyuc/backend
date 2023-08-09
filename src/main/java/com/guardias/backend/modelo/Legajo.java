package com.guardias.backend.modelo;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "legajo")
public class Legajo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLegajo;
    private Long idPersona;
    private Long idRevista;
    private LocalDate fechaInicio;
    private LocalDate fechaResolucion;
    private LocalDate fechaFinal;
    private boolean actual;


}
