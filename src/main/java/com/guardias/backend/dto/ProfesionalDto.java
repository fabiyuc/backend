package com.guardias.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class ProfesionalDto {
    
    @NotBlank
    private Long id;

    @NotBlank
    private String nombre;
    
    @NotBlank
    private String apellido;

    @NotBlank
    private String dni;

    public ProfesionalDto() {
    }

    public ProfesionalDto(@NotBlank Long id, @NotBlank String nombre, @NotBlank String apellido, @NotBlank String dni) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
    }

    public ProfesionalDto(@NotBlank String nombre, @NotBlank String apellido, @NotBlank String dni) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    
   
}
