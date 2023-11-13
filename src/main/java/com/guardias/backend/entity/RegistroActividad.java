package com.guardias.backend.entity;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.RequiredArgsConstructor;

@Entity(name = "registrosActividades")
@RequiredArgsConstructor
public class RegistroActividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String establecimiento;
    private String servicio;
    private Date fechaIngreso;
    private Date fechaEgreso;
    private String horaIngreso;
    private String horaEgreso;

    @ManyToOne(optional = true)
    @JoinColumn(name = "id_tipo_guardia")
    private TipoGuardia tipoGuardia;

    public RegistroActividad(String establecimiento, String servicio, Date fechaIngreso, Date fechaEgreso,
            String horaIngreso, String horaEgreso, TipoGuardia tipoGuardia) {
        this.establecimiento = establecimiento;
        this.servicio = servicio;
        this.fechaIngreso = fechaIngreso;
        this.fechaEgreso = fechaEgreso;
        this.horaIngreso = horaIngreso;
        this.horaEgreso = horaEgreso;
        this.tipoGuardia = tipoGuardia;
    }

}
