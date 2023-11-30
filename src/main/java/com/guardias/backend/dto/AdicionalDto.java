package com.guardias.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class AdicionalDto {

    @NotBlank
    private long id;
    @NotBlank
    private String nombre;

    public AdicionalDto() {
    }

    public AdicionalDto(long id, String nombre) {
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
