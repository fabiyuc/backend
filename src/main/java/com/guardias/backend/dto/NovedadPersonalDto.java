package com.guardias.backend.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NovedadPersonalDto {

    @NotNull
    private LocalDate fechaInicio; // obligatorio y no vacío para novedad personal
    private LocalDate fechaFinal;
    private boolean puedeRealizarGuardia;
    private boolean cobraSueldo;
    private boolean necesitaReemplazo;
    /* private boolean actual; // quitar */
    private boolean activo;

    /*
     * @NotBlank
     * private TipoNovedadEnum descripcion;
     */

    @NotNull
    private Long idPersona;

    private Long idSuplente;

    /*
     * @NotNull
     * private Long idArticulo; // no va
     * 
     * @NotNull
     * private Long idInciso; // no va
     */
    @NotNull
    private Long idTipoLicencia;
}