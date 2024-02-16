package com.guardias.backend.dto;

import java.time.LocalDate;
import java.util.Set;

import com.guardias.backend.entity.Efector;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AutoridadDto {

    @NotBlank
    private String nombre;

    @NotBlank
    private LocalDate fechaInicio;

    private LocalDate fechaFinal;

    @NotBlank
    private boolean esActual;

    @NotBlank
    private boolean esRegional;

    @NotBlank
    Set<Efector> efectores;

}
