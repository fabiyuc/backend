package com.guardias.backend.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;

public class LogDto {

    @NotBlank
    private LocalDateTime fecha;
    @NotBlank
    private String seccion;
    @NotBlank
    private String accion;

    public LogDto() {
    }

    public LogDto(@NotBlank String seccion, @NotBlank String accion) {
        this.fecha = LocalDateTime.now();
        this.seccion = seccion;
        this.accion = accion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
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
