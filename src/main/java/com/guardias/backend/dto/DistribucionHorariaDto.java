package com.guardias.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import com.guardias.backend.enums.DiasEnum;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistribucionHorariaDto {

    @NotNull
    private DiasEnum dia;
    @NotNull
    private LocalDate fechaInicio;
    private LocalDate fechaFinalizacion;
    @NotNull
    private LocalTime horaIngreso;
    @NotNull
    private BigDecimal cantidadHoras;
    @NotNull
    private Long idEfector;
    @NotNull
    private Long idPersona;
    private boolean activo;

}
