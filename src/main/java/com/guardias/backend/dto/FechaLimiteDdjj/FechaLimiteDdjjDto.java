package com.guardias.backend.dto.FechaLimiteDdjj;

import java.time.LocalDate;

import com.guardias.backend.enums.MesesEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Datos para crear o modificar una fecha límite.
 * En la modificación solo se toman fechaLimite, motivo e idUsuario;
 * mes, año y tipo de guardia se mantienen del registro vigente.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FechaLimiteDdjjDto {
    private MesesEnum mes;
    private Integer anio;
    private Long idTipoGuardia;
    private LocalDate fechaLimite;
    private String motivo;
    private Long idUsuario;
}
