package com.guardias.backend.modelo;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "legajo")

public class Legajo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPersona;

    @Column(name = "idRevista")
    private Long idRevista;

    @Column(name = "idLegajo")
    private Long idLegajo;

    @Column(name = "fechaInicio")
    private Date fechaInicio;

    @Column(name = "fechaResolucion")
    private Date fechaResolucion;

    @Column(name = "fechaFinal")
    private Date fechaFinal;

    @Column(name = "actual")
    private Boolean actual;

    public Legajo() {
    }

    public Legajo(Long idPersona, Long idRevista, Long idLegajo, Date fechaInicio, Date fechaResolucion,
            Date fechaFinal, Boolean actual) {
        this.idPersona = idPersona;
        this.idRevista = idRevista;
        this.idLegajo = idLegajo;
        this.fechaInicio = fechaInicio;
        this.fechaResolucion = fechaResolucion;
        this.fechaFinal = fechaFinal;
        this.actual = actual;
    }

    public Long getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Long idPersona) {
        this.idPersona = idPersona;
    }

    public Long getIdRevista() {
        return idRevista;
    }

    public void setIdRevista(Long idRevista) {
        this.idRevista = idRevista;
    }

    public Long getIdLegajo() {
        return idLegajo;
    }

    public void setIdLegajo(Long idLegajo) {
        this.idLegajo = idLegajo;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaResolucion() {
        return fechaResolucion;
    }

    public void setFechaResolucion(Date fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public Boolean getActual() {
        return actual;
    }

    public void setActual(Boolean actual) {
        this.actual = actual;
    }

}
