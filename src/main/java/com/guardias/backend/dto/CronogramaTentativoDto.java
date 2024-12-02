package com.guardias.backend.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CronogramaTentativoDto {
    
    @NotBlank
    private LocalDate fechaIngreso;
    @NotBlank
    private LocalDate fechaEgreso;
    @NotBlank
    private LocalTime horaIngreso;
    @NotBlank
    private LocalTime horaEgreso;

    private Boolean activo;
    
    private Boolean aceptado;

    @NotBlank
    private Long idTipoGuardia;

    @NotBlank
    private Long idAsistencial;
    
    @NotBlank
    private Long idEfector;

    private String observacion;
}
