package com.guardias.backend.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NovedadPersonalDto {

    @NotNull
    private LocalDate fechaInicio;
    private LocalDate fechaFinal;
    private boolean puedeRealizarGuardia;
    private boolean cobraSueldo;
    private boolean necesitaReemplazo;
    private boolean actual;
    private boolean activo;

    @NotBlank
    private String descripcion;

    @NotNull
    private Long idPersona;

    @NotNull
    private Long idSuplente;
    
    @NotNull
    private Long idArticulo;

    @NotNull
    private Long idInciso;
}