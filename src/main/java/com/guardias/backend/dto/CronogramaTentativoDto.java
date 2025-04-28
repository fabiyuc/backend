package com.guardias.backend.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.guardias.backend.enums.AutorizadoTentativoEnum;

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

    private boolean activo;

    private boolean aceptado;

    private AutorizadoTentativoEnum autorizado;

    @NotBlank
    private Long idTipoGuardia;

    @NotBlank
    private Long idAsistencial;

    @NotBlank
    private Long idServicio;

    @NotBlank
    private Long idEfector;

    private String observacion;
}
