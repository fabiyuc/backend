package com.guardias.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsignacionHorasEfectorDto  {
    
    private BigDecimal horasAsignadas;
    private LocalDate fechaInicio;
    private LocalDate fechaFinalizacion;
    private boolean activo;

    @NotNull
    private Long idLegajo;

    @NotNull
    private Long idEfector;
}
