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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

}
