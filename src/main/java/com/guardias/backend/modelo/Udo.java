package com.guardias.backend.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

//@Entity
//@Table(name = "udo")
public class Udo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUdo;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "nivel")
    private int nivel;

    @Column(name = "domicilio")
    private String domicilio;

    @Column(name = "region")
    private String region;

    @Column(name = "localidad")
    private String localidad;

    public Udo() {
    }

    public Udo(Long idUdo, String nombre, String tipo, int nivel, String domicilio, String region, String localidad) {
        this.idUdo = idUdo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.nivel = nivel;
        this.domicilio = domicilio;
        this.region = region;
        this.localidad = localidad;
    }

    public Long getIdUdo() {
        return idUdo;
    }

    public void setIdUdo(Long idUdo) {
        this.idUdo = idUdo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

}