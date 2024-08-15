package com.guardias.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BonoUtiDto {

    private boolean activo;

    private String documentoLegal;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private BigDecimal monto;
    
}
