package com.guardias.backend.dto;

import java.sql.Date;
import java.sql.Time;

import com.guardias.backend.modelo.TipoGuardia;

import jakarta.validation.constraints.NotBlank;

public class RegistroActividadDto {

    @NotBlank
    private Long id;

    @NotBlank
    private String establecimiento;

    @NotBlank
    private String servicio;

    @NotBlank
    private Date fechaIngreso;

    @NotBlank
    private Date fechaEgreso;

    @NotBlank
    private Time horaIngreso;

    @NotBlank
    private Time horaEgreso;

    @NotBlank
    private TipoGuardia tipoGuardia;

    public RegistroActividadDto() {
    }

    public RegistroActividadDto(@NotBlank String establecimiento, @NotBlank String servicio,
            @NotBlank Date fechaIngreso, @NotBlank Date fechaEgreso, @NotBlank Time horaIngreso,
            @NotBlank Time horaEgreso, @NotBlank TipoGuardia tipoGuardia) {
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

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaEgreso() {
        return fechaEgreso;
    }

    public void setFechaEgreso(Date fechaEgreso) {
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

    public TipoGuardia getTipoGuardia() {
        return tipoGuardia;
    }

    public void setTipoGuardia(TipoGuardia tipoGuardia) {
        this.tipoGuardia = tipoGuardia;
    }

    
}
