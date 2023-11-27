package com.guardias.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class CategoriaDto {

    @NotBlank
    private long id;
    @NotBlank
    private String nombre;

    public CategoriaDto() {
    }

    public CategoriaDto(@NotBlank long id, @NotBlank String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
