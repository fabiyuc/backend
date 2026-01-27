package com.guardias.backend.dto.valorGuardia;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MontoDto {
    private BigDecimal lav;
    private BigDecimal sdf;
}
