package com.guardias.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class PaisDto {

    @NotBlank
    private String nombre;
    @NotBlank
    private String nacionalidad;
    private String codigo;

    public PaisDto() {
    }

    public PaisDto(String nombre, String nacionalidad, String codigo) {
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
        this.codigo = codigo;
    }
}
