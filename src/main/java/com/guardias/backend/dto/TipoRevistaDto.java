package com.guardias.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class TipoRevistaDto {

    @NotBlank
    private Long id;

    @NotBlank
    private String nombre;

    public TipoRevistaDto() {
    }

    public TipoRevistaDto(@NotBlank String nombre) {
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    

}
