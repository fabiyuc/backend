package com.guardias.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class AdicionalDto {

    @NotBlank
    private int id;
    @NotBlank
    private String nombre;

    public AdicionalDto() {
    }

    public AdicionalDto(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
