package com.guardias.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class ProfesionDto {

    @NotBlank
    private Long id;

    @NotBlank
    private String nombre;

    @NotBlank
    private Boolean esAsistencial;

    @NotBlank
    private String matriculaNacional;

    @NotBlank
    private String matriculaProvincial;

    public ProfesionDto() {
    }

    public ProfesionDto(@NotBlank String nombre, @NotBlank Boolean esAsistencial, @NotBlank String matriculaNacional,
            @NotBlank String matriculaProvincial) {
        this.nombre = nombre;
        this.esAsistencial = esAsistencial;
        this.matriculaNacional = matriculaNacional;
        this.matriculaProvincial = matriculaProvincial;
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

    public String getMatriculaNacional() {
        return matriculaNacional;
    }

    public void setMatriculaNacional(String matriculaNacional) {
        this.matriculaNacional = matriculaNacional;
    }

    public String getMatriculaProvincial() {
        return matriculaProvincial;
    }

    public void setMatriculaProvincial(String matriculaProvincial) {
        this.matriculaProvincial = matriculaProvincial;
    }

}
