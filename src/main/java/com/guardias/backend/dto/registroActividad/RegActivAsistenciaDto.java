package com.guardias.backend.dto.registroActividad;

import java.time.LocalDate;
import java.time.LocalTime;

import com.guardias.backend.enums.TipoGuardiaEnum;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class RegActivAsistenciaDto {

    private Long id;

    @NotBlank
    private LocalDate fechaIngreso;
    private LocalDate fechaEgreso;

    private LocalDate fechaRegistroIngreso;
    private LocalDate fechaRegistroEgreso;

    @NotBlank
    private LocalTime horaIngreso;
    private LocalTime horaEgreso;

    private LocalTime horaRegistroIngreso;
    private LocalTime horaRegistroEgreso;

    @NotBlank
    private TipoGuardiaEnum tipoGuardia;

    private Boolean activo;

    @NotBlank
    private Long idAsistencial;
    @NotBlank
    private String servicio;
    @NotBlank
    private Long idEfector;
    private String usuarioIngreso;
    private Long idUsuarioEgreso;

    private String motivoIngreso;
    private String motivoEgreso;

}
