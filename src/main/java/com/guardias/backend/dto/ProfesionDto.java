package com.guardias.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class ProfesionDto {

    @NotBlank
    private Long id;

    @NotBlank
    private String nombre;

    @NotBlank
    private Boolean esAsistencial;

    public ProfesionDto() {
    }

    public ProfesionDto(@NotBlank String nombre, @NotBlank Boolean esAsistencial) {
        this.nombre = nombre;
        this.esAsistencial = esAsistencial;

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

    public Boolean getEsAsistencial() {
        return esAsistencial;
    }

    public void setEsAsistencial(Boolean esAsistencial) {
        this.esAsistencial = esAsistencial;
    }

}
