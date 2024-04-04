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
    private boolean actual;
    private String descripcion;

    private boolean activo;

    @NotEmpty
    private Long idPersona;
    private Long idReemplazante;
    private Long idArticulo;
    private Long idInciso;
    private Long idExtensionLicencia;
}