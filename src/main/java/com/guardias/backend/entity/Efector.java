package com.guardias.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Table;

@MappedSuperclass
@Table(name = "efectores")
public class Efector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idEfector;
    private String nombre;
    private String domicilio;
    private String telefono;
    private boolean estado;
    private String observacion;

    @Column(name = "id_region")
    private Long idRegion;

    @Column(name = "id_localidad")
    private Long idLocalidad;

    public Efector() {
    }

    public Efector(long idEfector, String nombre, String domicilio, String telefono, boolean estado, String observacion,
            long idRegion, long idLocalidad) {
        this.idEfector = idEfector;
        this.nombre = nombre;
        this.domicilio = domicilio;
        this.telefono = telefono;
        this.estado = estado;
        this.observacion = observacion;
        this.idRegion = idRegion;
        this.idLocalidad = idLocalidad;
    }

    public Long getIdEfector() {
        return idEfector;
    }

    public void setId(long id) {
        this.idEfector = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public long getIdRegion() {
        return idRegion;
    }

    public void setIdRegion(long idRegion) {
        this.idRegion = idRegion;
    }

    public long getIdLocalidad() {
        return idLocalidad;
    }

    public void setIdLocalidad(long idLocalidad) {
        this.idLocalidad = idLocalidad;
    }

}
