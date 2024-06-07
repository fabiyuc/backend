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
    private Long idUsuario;
    private LocalDate fechaRegistroIngreso;
    private LocalTime horaRegistroIngreso;
    private LocalDate fechaRegistroEgreso;
    private LocalTime horaRegistroEgreso;

}
