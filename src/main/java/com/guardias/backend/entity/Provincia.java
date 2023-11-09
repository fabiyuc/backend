package com.guardias.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Provincia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nombre;
    private String gentilicio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pais")
    Pais pais;

    public Provincia() {
    }

    public Provincia(int id, String nombre, String gentilicio) {
        this.id = id;
        this.nombre = nombre;
        this.gentilicio = gentilicio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
    /*
     * public Pais getPais() {
     * return pais;
     * }
     */
}
