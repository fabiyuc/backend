package com.guardias.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.guardias.backend.enums.TipoGuardiaEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValorGuardiaBaseDto {
    
    private boolean activo;
    private TipoGuardiaEnum tipoGuardia;
    private int nivelComplejidad;
    private List<String> efectores;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private BigDecimal total;
    private Long idValorGmi;
    private Long idBonoUti;
    
}
