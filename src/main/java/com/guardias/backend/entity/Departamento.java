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

@Entity(name = "departamentos")
public class Departamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(50)")
    private String nombre;

    @Column(columnDefinition = "VARCHAR(10)")
    private String codigoPostal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_provincia")
    @JsonIgnore
    Provincia provincia;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "departamento", cascade = CascadeType.ALL)
    List<Localidad> localidad;

    public Departamento() {
    }

    public Departamento(String nombre, String codigoPostal) {
        this.nombre = nombre;
        this.codigoPostal = codigoPostal;
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

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public List<Localidad> getLocalidad() {
        return localidad;
    }

    public void setLocalidad(List<Localidad> localidad) {
        this.localidad = localidad;
    }

    public Provincia getProvincia() {
        return provincia;
    }

}
