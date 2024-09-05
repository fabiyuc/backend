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
public class ValorGuardiaCargoYagrupDto extends ValorGuardiaBaseDto {
    
    private BigDecimal decreto1178Lav;
    private BigDecimal decreto1178Sdf;
    private BigDecimal decreto1657Lav;
    private BigDecimal decreto1657Sdf; 
}
