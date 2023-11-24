package com.guardias.backend.dto;

import java.sql.Date;

import com.guardias.backend.entity.Profesion;
import com.guardias.backend.entity.Revista;
import com.guardias.backend.entity.Suspencion;

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

    @NotBlank
    private String matriculaNacional;

    @NotBlank
    private String matriculaProvincial;

    @NotBlank
    private Profesion profesion;
    
    @NotBlank
    private Suspencion suspencion;
    
    @NotBlank
    private Revista revista;


    public LegajoDto() {
    }

    public LegajoDto(@NotBlank Date fechaInicio, @NotBlank Date fechaFinal, @NotBlank boolean esActual,
            @NotBlank boolean esLegal, @NotBlank String matriculaNacional, @NotBlank String matriculaProvincial,
            @NotBlank Profesion profesion, @NotBlank Suspencion suspencion, @NotBlank Revista revista) {
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
        this.esActual = esActual;
        this.esLegal = esLegal;
        this.matriculaNacional = matriculaNacional;
        this.matriculaProvincial = matriculaProvincial;
        this.profesion = profesion;
        this.suspencion = suspencion;
        this.revista = revista;
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

    public String getMatriculaNacional() {
        return matriculaNacional;
    }

    public void setMatriculaNacional(String matriculaNacional) {
        this.matriculaNacional = matriculaNacional;
    }

    public String getMatriculaProvincial() {
        return matriculaProvincial;
    }

    public void setMatriculaProvincial(String matriculaProvincial) {
        this.matriculaProvincial = matriculaProvincial;
    }

    public Profesion getProfesion() {
        return profesion;
    }

    public void setProfesion(Profesion profesion) {
        this.profesion = profesion;
    }

    public Suspencion getSuspencion() {
        return suspencion;
    }

    public void setSuspencion(Suspencion suspencion) {
        this.suspencion = suspencion;
    }

    public Revista getRevista() {
        return revista;
    }

    public void setRevista(Revista revista) {
        this.revista = revista;
    }

    
    
}
