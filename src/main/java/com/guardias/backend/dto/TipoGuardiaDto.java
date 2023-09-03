package com.guardias.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class TipoGuardiaDto {
    @NotBlank
    private String nombre;
    @NotBlank
    private String descripcion;

    public TipoGuardiaDto() {
    }
    
    public TipoGuardiaDto(@NotBlank String nombre, @NotBlank String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
