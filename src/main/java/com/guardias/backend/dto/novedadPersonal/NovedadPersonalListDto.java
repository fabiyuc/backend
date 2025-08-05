package com.guardias.backend.dto.novedadPersonal;

import java.time.LocalDate;
import java.time.LocalTime;

import com.guardias.backend.dto.tipoLicencia.TipoLicenciaListDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NovedadPersonalListDto {
    private Long id;
    private LocalDate fechaInicio;
    private LocalDate fechaFinal;
    private LocalTime horaInicio;
    private LocalTime horaFinal;
    private TipoLicenciaListDto tipoLicencia;

}
