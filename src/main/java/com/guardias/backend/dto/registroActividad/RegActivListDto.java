package com.guardias.backend.dto.registroActividad;

import java.time.LocalDate;
import java.time.LocalTime;

import com.guardias.backend.dto.servicio.ServicioSummaryDto;
import com.guardias.backend.dto.sumaHoras.SumaHorasListDto;
import com.guardias.backend.dto.tipoGuardia.TipoGuardiaListDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegActivListDto {

    private Long id;
    private LocalDate fechaIngreso;
    private LocalDate fechaEgreso;
    private LocalTime horaIngreso;
    private LocalTime horaEgreso;
    private TipoGuardiaListDto tipoGuardia;
    private ServicioSummaryDto servicio;
    private SumaHorasListDto horasRealizadas;
    

}
