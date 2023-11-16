package com.guardias.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class PaisDto {

    @NotBlank
    private Long id;

    @NotBlank
    private String nombre;

    @NotBlank
    private String codigo;

    public PaisDto() {
    }

    public PaisDto(@NotBlank String nombre) {
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

/* package com.guardias.backend.dto;

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
}
*/