package com.guardias.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class AdicionalDto {

    @NotBlank
    private String nombre;

    public AdicionalDto() {
    }

    public AdicionalDto(@NotBlank String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
