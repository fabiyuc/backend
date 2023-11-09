package com.guardias.backend.dto;

import java.sql.Date;

import jakarta.validation.constraints.NotBlank;

public class SuspencionDto {

    @NotBlank
    private Long id;

    @NotBlank
    private String descripcion;

    @NotBlank
    private Date fechaInicio;

    @NotBlank
    private Date fechaFin;

    public SuspencionDto() {
    }

    public SuspencionDto(@NotBlank String descripcion, @NotBlank Date fechaInicio, @NotBlank Date fechaFin) {
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    
    
    
}
