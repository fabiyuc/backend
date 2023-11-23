package com.guardias.backend.dto;

import java.sql.Date;

import jakarta.validation.constraints.NotBlank;

public class LogDto {

    @NotBlank
    private Date fecha;
    @NotBlank
    private String seccion;
    @NotBlank
    private String accion;

    public LogDto() {
    }

    public LogDto(@NotBlank Date fecha, @NotBlank String seccion, @NotBlank String accion) {
        this.fecha = fecha;
        this.seccion = seccion;
        this.accion = accion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

}
