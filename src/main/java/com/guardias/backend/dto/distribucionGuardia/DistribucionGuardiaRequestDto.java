package com.guardias.backend.dto.distribucionGuardia;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistribucionGuardiaRequestDto {
    
    private Long idPersona;
    private Long idEfector;
    private String tipoGuardia;
    private LocalDate fechaInicio;
    private LocalDate fechaFinalizacion;// se calcula 
    private LocalTime horaIngreso;
    private int cantidadHoras;

}
