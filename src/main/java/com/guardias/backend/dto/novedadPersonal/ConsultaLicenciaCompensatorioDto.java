package com.guardias.backend.dto.novedadPersonal;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultaLicenciaCompensatorioDto {
    @NotNull
    private Long idPersona;
    
    @NotNull
    private LocalDate fechaInicioConsulta;
    
    @NotNull
    private LocalTime horaInicioConsulta;
    
    @NotNull
    private LocalDate fechaFinConsulta;
    
    @NotNull
    private LocalTime horaFinConsulta;
}
