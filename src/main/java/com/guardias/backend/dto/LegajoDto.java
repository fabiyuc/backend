package com.guardias.backend.dto;

import java.time.LocalDate;

import com.guardias.backend.entity.Profesion;
import com.guardias.backend.entity.Revista;
import com.guardias.backend.entity.Suspencion;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LegajoDto {

    @NotBlank
    private LocalDate fechaInicio;
    private LocalDate fechaFinal;
    private boolean esActual;
    private boolean esLegal;
    private String matriculaNacional;
    private String matriculaProvincial;
    private Profesion profesion;
    private Suspencion suspencion;
    private Revista revista;
}
