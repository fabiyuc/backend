package com.guardias.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "distribucionesConsultorios")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class DistribucionConsultorio extends DistribucionHoraria {
    
    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_servicio")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "distribucionesConsultorios" })
    private Servicio servicio;

    @Column(columnDefinition = "VARCHAR(50)")
    private String tipoConsultorio;

    @Column(columnDefinition = "VARCHAR(50)")
    private String lugar; // si indica puestos de salud, debe poder cargar el puesto de salud que corresponda al hospital en cuestion


    
    /* @Column(columnDefinition = "VARCHAR(50)")
    private String lugar; // donde realiza el consultorio(lugar interno del hospital)
    @Column(columnDefinition = "VARCHAR(50)")
    private String especialidad; // especialidad del consultorio no del profesional
    private int cantidadTurnos; */
}
