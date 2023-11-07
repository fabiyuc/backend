package com.guardias.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class DepartamentoDto {

    @NotBlank
    private String nombre;
    private String codigoPostal;

    public DepartamentoDto() {
    }

    public DepartamentoDto(String nombre, String codigoPostal) {
        this.nombre = nombre;
        this.codigoPostal = codigoPostal;
    }

}
