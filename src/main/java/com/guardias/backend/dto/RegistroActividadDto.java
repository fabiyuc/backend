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
public class RegistroActividadDto {

    @NotBlank
    private LocalDate fechaIngreso;

    private LocalDate fechaEgreso;

    @NotBlank
    private LocalTime horaIngreso;

    private LocalTime horaEgreso;

    @NotBlank
    private Long idTipoGuardia;

    private Boolean activo;

    @NotBlank
    private Long idAsistencial;
    @NotBlank
    private Long idServicio;
    @NotBlank
    private Long idEfector;
    private Long idRegistroMensual;
    private Long idRegistrosPendientes;
    private Long idUsuarioIngreso;
    private Long idUsuarioEgreso;
}
// dame el json completo
/*
 * {
 * "fechaIngreso": "2021-09-01",
 * "fechaEgreso": "2021-09-01",
 * "horaIngreso": "10:00",
 * "horaEgreso": "18:00",
 * "idTipoGuardia": 1,
 * "activo": true,
 * "idAsistencial": 1,
 * "idServicio": 1,
 * "idEfector": 1,
 * "idRegistroMensual": 1,
 * "idRegistrosPendientes": 1,
 * "idUsuario": 1
 * }
 */