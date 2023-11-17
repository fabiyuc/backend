package com.guardias.backend.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity(name = "provincias")
public class Provincia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "VARCHAR(25)")
    private String nombre;
    @Column(columnDefinition = "VARCHAR(25)")
    private String gentilicio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pais")
    @JsonIgnore
    Pais pais;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "provincia", cascade = CascadeType.ALL)
    List<Departamento> departamento;

    public Provincia() {
    }

    public Provincia(String nombre, String gentilicio) {
        this.nombre = nombre;
        this.gentilicio = gentilicio;
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

    public String getGentilicio() {
        return gentilicio;
    }

    public void setGentilicio(String gentilicio) {
        this.gentilicio = gentilicio;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public List<Departamento> getDepartamento() {
        return departamento;
    }

    public void setDepartamento(List<Departamento> departamento) {
        this.departamento = departamento;
    }

    public Pais getPais() {
        return pais;
    }

}
