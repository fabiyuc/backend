package com.guardias.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity

@Table(name = "efectores")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_efector")
@DiscriminatorValue("EFECTOR")
public class Efector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "VARCHAR(25)")
    private String nombre;
    @Column(columnDefinition = "VARCHAR(50)")
    private String domicilio;
    @Column(columnDefinition = "VARCHAR(15)")
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
        this.id = idEfector;
        this.nombre = nombre;
        this.domicilio = domicilio;
        this.telefono = telefono;
        this.estado = estado;
        this.observacion = observacion;
        this.idRegion = idRegion;
        this.idLocalidad = idLocalidad;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
