package com.guardias.backend.dto;

import java.time.LocalDate;

import com.guardias.backend.entity.Asistencial;
import com.guardias.backend.entity.NoAsistencial;
import com.guardias.backend.entity.Profesion;
import com.guardias.backend.entity.Revista;
import com.guardias.backend.entity.Suspencion;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LegajoDto {

    @NotBlank
    private LocalDate fechaInicio;

    private LocalDate fechaFinal;

    @NotBlank
    private Boolean actual;

    @NotBlank
    private Boolean legal;

    private String matriculaNacional;

    private String matriculaProvincial;

    @NotBlank
    private Profesion profesion;

    @NotBlank
    private Suspencion suspencion;

    @NotBlank
    private Revista revista;

    @NotBlank
    private Asistencial asistencial;

    @NotBlank
    private NoAsistencial noAsistencial;
}
