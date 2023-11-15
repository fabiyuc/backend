package com.guardias.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "hospitales")
public class Hospital extends Efector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean esCabecera;

    @Column(name = "nivel_complejidad")
    private int nivelComplejidad;

    public Hospital() {
    }

    public Hospital(boolean esCabecera, int nivelComplejidad) {
        this.esCabecera = esCabecera;
        this.nivelComplejidad = nivelComplejidad;
    }

    public boolean isEsCabecera() {
        return esCabecera;
    }

    public void setEsCabecera(boolean esCabecera) {
        this.esCabecera = esCabecera;
    }

    public int getNivelComplejidad() {
        return nivelComplejidad;
    }

    public void setNivelComplejidad(int nivelComplejidad) {
        this.nivelComplejidad = nivelComplejidad;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
