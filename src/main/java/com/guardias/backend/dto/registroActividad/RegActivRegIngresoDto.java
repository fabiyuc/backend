package com.guardias.backend.dto.registroActividad;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegActivRegIngresoDto {
    private Long idAsistencial;
    private Long idEfector;
    private Long idTipoGuardia;
    private Long idServicio;
    private LocalDate fechaIngreso;
    private LocalTime horaIngreso;
}
