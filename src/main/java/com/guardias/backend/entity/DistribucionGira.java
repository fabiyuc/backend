package com.guardias.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "distribucionesGiras")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class DistribucionGira extends DistribucionHoraria {
    
    @Column(columnDefinition = "VARCHAR(50)")
    private String puestoSalud;

    /* @Column(columnDefinition = "VARCHAR(100)")
    private String descripcion; */
}
