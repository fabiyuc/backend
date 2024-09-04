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
    
    private Long idServicio;
    private String tipoConsultorio;
    private String lugar;
    
    //private int cantidadTurnos;
    //private String especialidad;
}