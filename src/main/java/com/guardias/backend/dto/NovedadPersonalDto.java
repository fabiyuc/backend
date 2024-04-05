package com.guardias.backend.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotEmpty;
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
    private boolean activa;
    private String descripcion;
    private Long idExtensionLicencia;
    private boolean activo;

    @NotEmpty
    private Long idPersona;
    private Long idSuplente;
    private Long idArticulo;
    private Long idInciso;
}