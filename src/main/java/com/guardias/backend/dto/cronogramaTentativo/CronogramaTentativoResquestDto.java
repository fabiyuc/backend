package com.guardias.backend.dto.cronogramaTentativo;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CronogramaTentativoResquestDto {
    private Long idAsistencial;
    private Long idEfector;
    private String tipoGuardia;
    private LocalDate fechaIngreso;
    private LocalTime horaIngreso;
    private LocalTime horaEgreso;
}
