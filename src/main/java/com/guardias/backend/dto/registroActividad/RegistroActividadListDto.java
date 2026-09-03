package com.guardias.backend.dto.registroActividad;

import java.time.LocalDate;
import java.time.LocalTime;

import com.guardias.backend.dto.asistencial.AsistencialListForRmensualDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistroActividadListDto {
    
    private Long id;
        private AsistencialListForRmensualDto asistencial;
        private Long idTipoGuardia;
        private LocalDate fechaIngreso;
        private LocalDate fechaEgreso;
        private LocalTime horaIngreso;
        private LocalTime horaEgreso;
        private Boolean esGuardiaIncompleta;
}
