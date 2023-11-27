package com.guardias.backend.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity(name = "Feriados")
public class Feriado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.DATE)
    private LocalDate dia;
    @Column(columnDefinition = "VARCHAR(25)")
    private String motivo;
    @Column(columnDefinition = "VARCHAR(25)")
    private String tipoFeriado; // Feriado Nacional/Provincial - Asueto administrativo - Feriado Puente
                                // Turistico
    @Column(columnDefinition = "VARCHAR(50)")
    private String descripcion;

    public Feriado() {
    }

    public Feriado(LocalDate dia, String motivo, String tipoFeriado, String descripcion) {
        this.dia = dia;
        this.motivo = motivo;
        this.tipoFeriado = tipoFeriado;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDia() {
        return dia;
    }

    public void setDia(LocalDate dia) {
        this.dia = dia;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getTipoFeriado() {
        return tipoFeriado;
    }

    public void setTipoFeriado(String tipoFeriado) {
        this.tipoFeriado = tipoFeriado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
