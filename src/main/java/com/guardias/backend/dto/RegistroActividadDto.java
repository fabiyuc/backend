package com.guardias.backend.dto;

import java.sql.Time;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;

public class RegistroActividadDto {
    
    @NotBlank
    private Long id;

    @NotBlank
    private String establecimiento;

    @NotBlank
    private String servicio;

    @NotBlank
    private LocalDate fechaIngreso;

    @NotBlank
    private LocalDate fechaEgreso;
    
    @NotBlank
    private Time horaIngreso;
    
    @NotBlank
    private Time horaEgreso;

    public RegistroActividadDto() {
    }

    public RegistroActividadDto(@NotBlank String establecimiento, @NotBlank String servicio,
            @NotBlank LocalDate fechaIngreso, @NotBlank LocalDate fechaEgreso, @NotBlank Time horaIngreso,
            @NotBlank Time horaEgreso) {
        this.establecimiento = establecimiento;
        this.servicio = servicio;
        this.fechaIngreso = fechaIngreso;
        this.fechaEgreso = fechaEgreso;
        this.horaIngreso = horaIngreso;
        this.horaEgreso = horaEgreso;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(String establecimiento) {
        this.establecimiento = establecimiento;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public LocalDate getFechaEgreso() {
        return fechaEgreso;
    }

    public void setFechaEgreso(LocalDate fechaEgreso) {
        this.fechaEgreso = fechaEgreso;
    }

    public Time getHoraIngreso() {
        return horaIngreso;
    }

    public void setHoraIngreso(Time horaIngreso) {
        this.horaIngreso = horaIngreso;
    }

    public Time getHoraEgreso() {
        return horaEgreso;
    }

    public void setHoraEgreso(Time horaEgreso) {
        this.horaEgreso = horaEgreso;
    }

    

    
}
