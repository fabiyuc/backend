package com.guardias.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class TipoCargoDto {

    @NotBlank
    private String nombre;
    @NotBlank
    private String descripcion;
    @NotBlank
    private boolean eshospitalario;

    public TipoCargoDto() {
    }

    public TipoCargoDto(String nombre, String descripcion, boolean eshospitalario) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.eshospitalario = eshospitalario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isEshospitalario() {
        return eshospitalario;
    }

    public void setEshospitalario(boolean eshospitalario) {
        this.eshospitalario = eshospitalario;
    }

    public Long getId() {
        return null;
    }
}
