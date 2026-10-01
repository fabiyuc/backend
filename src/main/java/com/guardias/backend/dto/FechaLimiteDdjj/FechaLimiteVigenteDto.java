package com.guardias.backend.dto.FechaLimiteDdjj;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Fecha límite que se aplica a un mes/año/tipo de guardia.
 * definidaManualmente = false indica que no hay una fecha cargada y se usa la
 * regla por defecto (lunes de la segunda semana del mes siguiente).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FechaLimiteVigenteDto {
    private LocalDate fechaLimite;
    private boolean definidaManualmente;
    private Long idFechaLimite;
}
