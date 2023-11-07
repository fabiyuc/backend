package com.guardias.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class LocalidadDto {

    @NotBlank
    private String nombre;

    @NotBlank
    private int idDepartamento;

    public LocalidadDto() {
    }

    public LocalidadDto(@NotBlank String nombre, @NotBlank int idDepartamento) {
        this.nombre = nombre;
        this.idDepartamento = idDepartamento;
    }

}
