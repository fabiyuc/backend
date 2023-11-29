package com.guardias.backend.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity(name = "paises")
public class Pais {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(25)")
    private String nombre;

    @Column(columnDefinition = "VARCHAR(25)")
    private String nacionalidad;

    @Column(columnDefinition = "VARCHAR(5)")
    private String codigo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pais", cascade = CascadeType.ALL)
    List<Provincia> provincia;

    public Pais() {
    }

    public Pais(String nombre, String nacionalidad, String codigo) {
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
        this.codigo = codigo;
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

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public List<Provincia> getProvincia() {
        return provincia;
    }

    public void setProvincia(List<Provincia> provincia) {
        this.provincia = provincia;
    }

}
