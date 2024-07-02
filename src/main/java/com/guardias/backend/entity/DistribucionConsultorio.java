package com.guardias.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "distribucionesConsultorios")
@Data
@EqualsAndHashCode(callSuper = false) // no modifica los metodos Equals y Hash de la supereclase, pero si los utiliza
@AllArgsConstructor
@NoArgsConstructor
public class DistribucionConsultorio extends DistribucionHoraria {
    @Column(columnDefinition = "VARCHAR(50)")
    private String lugar; // donde realiza el consultorio(lugar interno del hospital)
    @Column(columnDefinition = "VARCHAR(50)")
    private String especialidad; // especialidad del consultorio no del profesional
    private int cantidadTurnos;
}
