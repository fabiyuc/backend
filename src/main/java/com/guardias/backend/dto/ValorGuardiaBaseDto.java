package com.guardias.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.guardias.backend.entity.Hospital;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValorGuardiaBaseDto {
    
    private boolean activo;
    private String familiaValorBase;
    private int nivelComplejidad;
    private boolean esServicioCritico;
    private List<Hospital> hospitales;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private BigDecimal total;
    private Long idValorGmi;
    private Long idBonoUti;
    
}
