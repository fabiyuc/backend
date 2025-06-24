package com.guardias.backend.dto.cronogramaTentativo;

import java.time.LocalDate;
import java.time.LocalTime;

import com.guardias.backend.dto.asistencial.AsistencialDetailDto;
import com.guardias.backend.enums.AutorizadoTentativoEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CronogramaTentativoListAtorizadoDto {
    private Long id;
    private AsistencialDetailDto asistencial; // Cambiado de Long a AsistencialDetailDto
    private Long idEfector;
    private String tipoGuardia;
    private LocalDate fechaIngreso;
    private LocalDate fechaEgreso;
    private LocalTime horaIngreso;
    private LocalTime horaEgreso;
    private AutorizadoTentativoEnum autorizado;
    private Long idServicio;
    private String motivoAutorizacion;
    private String motivoPediente;
    private Long idAutoridad;
}
