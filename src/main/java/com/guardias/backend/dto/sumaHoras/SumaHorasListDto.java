package com.guardias.backend.dto.sumaHoras;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SumaHorasListDto {
    private Long id;
    private float horasLav;
    private float horasSdf;
    private BigDecimal montoLav;
    private BigDecimal montoSdf;
    private BigDecimal montoTotal;
}
