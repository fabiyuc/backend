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
    private LocalDate fechaInicioConsulta;  // Fecha inicial del rango a verificar
    
    private LocalTime horaInicioConsulta;   // Hora inicial (opcional, si es null = todo el día)
    
    @NotNull
    private LocalDate fechaFinConsulta;     // Fecha final del rango a verificar
    
    private LocalTime horaFinConsulta;      // Hora final (opcional, si es null = todo el día)
}
