package com.guardias.backend.dto;

import java.sql.Date;
import jakarta.validation.constraints.NotBlank;

public class AutoridadDto {
    
    @NotBlank
    private Long id;

    @NotBlank
    private String nombre;

    @NotBlank
    private Date fechaInicio;

    @NotBlank
    private Date fechaFinal;
    
    @NotBlank
    private boolean esActual;
    
    @NotBlank
    private boolean esRegional;

    public AutoridadDto() {
    }

    public AutoridadDto(@NotBlank String nombre, @NotBlank Date fechaInicio, @NotBlank Date fechaFinal,
            @NotBlank boolean esActual, @NotBlank boolean esRegional) {
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
        this.esActual = esActual;
        this.esRegional = esRegional;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public boolean isEsActual() {
        return esActual;
    }

    public void setEsActual(boolean esActual) {
        this.esActual = esActual;
    }

    public boolean isEsRegional() {
        return esRegional;
    }

    public void setEsRegional(boolean esRegional) {
        this.esRegional = esRegional;
    }

    
}
