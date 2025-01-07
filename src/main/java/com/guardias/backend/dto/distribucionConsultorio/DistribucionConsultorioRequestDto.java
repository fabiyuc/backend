package com.guardias.backend.dto.distribucionConsultorio;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistribucionConsultorioRequestDto {
    
    private Long idPersona;
    private Long idEfector;
    private LocalDate fechaInicio;
    private LocalDate fechaFinalizacion;
    private LocalTime horaIngreso;
    private int cantidadHoras;
}
