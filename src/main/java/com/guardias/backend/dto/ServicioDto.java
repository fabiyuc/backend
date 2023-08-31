package com.guardias.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class ServicioDto {

    @NotBlank
    private String descripcion;

    public ServicioDto() {
    }

    public ServicioDto(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
