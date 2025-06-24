package com.guardias.backend.dto.registroActividad;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegActivMotivoDto {

    private Long id;
    private Long idAsistencial;
    private LocalDate fechaIngreso;
    private LocalDate fechaEgreso;
    private LocalTime horaIngreso;
    private LocalTime horaEgreso;
    private Long idUsuarioIngreso;
    private Long idUsuarioEgreso;
    private String motivoIngreso;
    private String motivoEgreso;
}
