package com.guardias.backend.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SumaHorasDto {
    private float horasLav;
    private float horasSdf;
   /*  private float bonoLav;
    private float bonoSdf; */
    private BigDecimal montoLav;
    private BigDecimal montoSdf;
    private BigDecimal montoTotal;
    private Long idRegistroMensual;
    private boolean activo;
}
