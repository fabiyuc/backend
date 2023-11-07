package com.guardias.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class RegionDto {

    @NotBlank
    private String nombre;

    public RegionDto() {
    }

    public RegionDto(String nombre) {
        this.nombre = nombre;
    }

}
