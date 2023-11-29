package com.guardias.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity(name = "profesionales")
@RequiredArgsConstructor
@AllArgsConstructor
public class Profesional extends Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "matricula_provincial", columnDefinition = "VARCHAR(10)")
    private String matriculaProvincial;

    @Column(name = "matricula_nacional", columnDefinition = "VARCHAR(10)")
    private String matriculaNacional;

    public Profesional(Long idPersona, String nombre, String apellido, int dni, String cuil, String sexo,
            String direccion, String telefono, String email, Long id, String matriculaProvincial,
            String matriculaNacional) {
        super(idPersona, nombre, apellido, dni, cuil, sexo, direccion, telefono, email);
        this.id = id;
        this.matriculaProvincial = matriculaProvincial;
        this.matriculaNacional = matriculaNacional;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatriculaProvincial() {
        return matriculaProvincial;
    }

    public void setMatriculaProvincial(String matriculaProvincial) {
        this.matriculaProvincial = matriculaProvincial;
    }

    public String getMatriculaNacional() {
        return matriculaNacional;
    }

    public void setMatriculaNacional(String matriculaNacional) {
        this.matriculaNacional = matriculaNacional;
    }

}