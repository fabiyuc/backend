package com.guardias.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class EspecialidadDto {
    @NotBlank
    private String nombre;

    public EspecialidadDto() {
    }

    public EspecialidadDto(@NotBlank String nombre) {
        this.nombre = nombre;
    }

}
