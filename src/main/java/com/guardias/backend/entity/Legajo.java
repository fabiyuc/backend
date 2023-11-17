package com.guardias.backend.entity;

import java.sql.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Legajo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date fechaInicio;
    private Date fechaFinal;
    private boolean esActual;
    private boolean esLegal;
    private String matriculaNacional;
    private String matriculaProvincial;

  /*   @ManyToOne(optional = true)
    @JoinColumn(name = "id_profesion")
    private Profesion profesion; */

    /* @ManyToOne(optional = true)
    @JoinColumn(name = "id_suspencion")
    private Suspencion suspenciones; */

    public Legajo(Date fechaInicio, Date fechaFinal, boolean esActual, boolean esLegal, String matriculaNacional,
            String matriculaProvincial) {
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
        this.esActual = esActual;
        this.esLegal = esLegal;
        this.matriculaNacional = matriculaNacional;
        this.matriculaProvincial = matriculaProvincial;
       // this.profesion = profesion;
       // this.suspenciones = suspenciones;
    }

    
    
}
