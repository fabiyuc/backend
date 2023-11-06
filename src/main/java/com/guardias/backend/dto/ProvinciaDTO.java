package com.guardias.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class ProvinciaDTO {

    @NotBlank
    private String nombre;
    private String gentilicio;

    public ProvinciaDTO() {
    }

    public ProvinciaDTO(@NotBlank String nombre, String gentilicio) {
        this.nombre = nombre;
        this.gentilicio = gentilicio;
    }

}
