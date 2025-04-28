package com.guardias.backend.dto.cronogramaTentativo;

import java.time.LocalDate;
import java.time.LocalTime;

import com.guardias.backend.enums.AutorizadoTentativoEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CronogramaTentativoSummaryDto {

    private Long id;

    private LocalDate fechaIngreso;

    private LocalDate fechaEgreso;

    private LocalTime horaIngreso;

    private LocalTime horaEgreso;

    private boolean activo;

    private boolean aceptado;

    private AutorizadoTentativoEnum autorizado;

    private Long idTipoGuardia;

    private Long idAsistencial;

    private Long idServicio;

    private Long idEfector;

    private String observacion;

}
