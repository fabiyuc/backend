package com.guardias.backend.dto;

import java.sql.Date;
import jakarta.validation.constraints.NotBlank;

public class LegajoDto {
    
    @NotBlank
    private Long id;

    @NotBlank
    private Date fechaInicio;

    @NotBlank
    private Date fechaFinal;
    
    @NotBlank
    private boolean esActual;
    
    @NotBlank
    private boolean esLegal;

    public LegajoDto() {
    }

    public LegajoDto(@NotBlank Date fechaInicio, @NotBlank Date fechaFinal, @NotBlank boolean esActual,
            @NotBlank boolean esLegal) {
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
        this.esActual = esActual;
        this.esLegal = esLegal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isEsLegal() {
        return esLegal;
    }

    public void setEsLegal(boolean esLegal) {
        this.esLegal = esLegal;
    }


    

}
