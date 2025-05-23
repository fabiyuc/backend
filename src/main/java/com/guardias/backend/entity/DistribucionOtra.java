package com.guardias.backend.entity;

import com.guardias.backend.enums.TipoDistribucionOtraEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "distribucionesOtras")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class DistribucionOtra extends DistribucionHoraria {
    
    @Column(columnDefinition = "VARCHAR(100)")
    private String descripcion;

     @Column(columnDefinition = "VARCHAR(50)")
    private String lugar;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(40)")
    private TipoDistribucionOtraEnum tipo;
}
