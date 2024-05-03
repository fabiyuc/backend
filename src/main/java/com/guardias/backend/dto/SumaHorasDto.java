package com.guardias.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SumaHorasDto {
    private float horasLav;
    private float horasSdf;
    private float bonoLav;
    private float bonoSdf;
    private Long idRegistroMensual;
    private boolean activo;
}
