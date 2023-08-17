package com.guardias.backend.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "profesion")
public class Profesion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProfesion;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "asistencial")
    private Boolean asistencial;

    public Profesion() {
    }

    public Profesion(Long idProfesion, String descripcion, Boolean asistencial) {
        this.idProfesion = idProfesion;
        this.descripcion = descripcion;
        this.asistencial = asistencial;
    }

    public Long getIdProfesion() {
        return idProfesion;
    }

    public void setIdProfesion(Long idProfesion) {
        this.idProfesion = idProfesion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getAsistencial() {
        return asistencial;
    }

    public void setAsistencial(Boolean asistencial) {
        this.asistencial = asistencial;
    }

}