package com.guardias.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DistribucionConsultorioDto extends DistribucionHorariaDto {
    private String lugar;
    private String especialidad;
    private int cantidadTurnos;
}