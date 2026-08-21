package com.guardias.backend.dto.asignacionHorasEfector;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleAsignacionEfectorDto {
    
    private Long idEfector;
    private String nombreEfector;
    private BigDecimal horasAsignadas;
    
}
