package com.guardias.backend.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ValorGuardiaExtraYcfDto extends ValorGuardiaBaseDto{
    
     private BigDecimal resolucion2575Lav;
    private BigDecimal resolucion2575Sdf;
}
