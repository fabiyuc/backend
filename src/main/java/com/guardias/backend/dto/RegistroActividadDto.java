package com.guardias.backend.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.guardias.backend.enums.TipoGuardiaEnum;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistroActividadDto {

    @NotBlank
    private String establecimiento;

    @NotBlank
    private String servicio;

    @NotBlank
    private LocalDate fechaIngreso;

    private LocalDate fechaEgreso;
    private boolean activo;

    @NotBlank
    private LocalTime horaIngreso;

    private LocalTime horaEgreso;

    @NotBlank
    private TipoGuardiaEnum tipoGuardia;

}
