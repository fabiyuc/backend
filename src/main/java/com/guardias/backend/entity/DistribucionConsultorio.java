package com.guardias.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    
    //falta relacion con servicio

    //falta tipo de consultorio

    @Column(columnDefinition = "VARCHAR(50)")
    private String lugar; // si indica puestos de salud, debe poder cargar el puesto de salud que corresponda al hospital en cuestion

    //falta turno o usar horaIngreso que está en DistribucionHoraria

    
    /* @Column(columnDefinition = "VARCHAR(50)")
    private String lugar; // donde realiza el consultorio(lugar interno del hospital)
    @Column(columnDefinition = "VARCHAR(50)")
    private String especialidad; // especialidad del consultorio no del profesional
    private int cantidadTurnos; */
}
