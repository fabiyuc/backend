package com.guardias.backend.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AutoridadDto {

    @NotBlank
    private String nombre;

    @NotNull
    private LocalDate fechaInicio;
    @NotNull
    private LocalDate fechaFinal;

    private boolean esActual;

    private boolean esRegional;

    private boolean activo;

    @NotNull
    private Long idEfector;
    // private Efector efector;

    @NotNull
    private Long idPersona;
    // private Person persona;

}
