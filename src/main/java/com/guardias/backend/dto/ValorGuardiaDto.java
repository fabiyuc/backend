package com.guardias.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValorGuardiaDto {
    private Long id;
    private boolean activo;
    private int nivelComplejidad;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    private BigDecimal decreto1178Lav;
    private BigDecimal decreto1178Sdf;

    private BigDecimal bono1657Lav;
    private BigDecimal bono1657Sdf;

    private BigDecimal bono1580Lav;
    private BigDecimal bono1580Sdf;

    private BigDecimal resolucion2575Lav;
    private BigDecimal resolucion2575Sdf;
    
    private BigDecimal total; 
}
