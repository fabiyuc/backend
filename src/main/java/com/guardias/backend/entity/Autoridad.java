package com.guardias.backend.entity;

import java.sql.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Autoridad {
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private Date fechaInicio;
    private Date fechaFinal;
    private boolean esActual;
    private boolean esRegional;
    
    public Autoridad(String nombre, Date fechaInicio, Date fechaFinal, boolean esActual, boolean esRegional) {
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
        this.esActual = esActual;
        this.esRegional = esRegional;
    }
    
    
}
