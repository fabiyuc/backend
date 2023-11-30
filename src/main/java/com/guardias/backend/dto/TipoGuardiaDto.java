package com.guardias.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class TipoGuardiaDto {

    @NotBlank
    private Long id;
    @NotBlank
    private String nombre;
    @NotBlank
    private String descripcion;

    public TipoGuardiaDto() {
    }

    public TipoGuardiaDto(@NotBlank Long id, @NotBlank String nombre, @NotBlank String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public TipoGuardiaDto(@NotBlank String nombre, @NotBlank String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
