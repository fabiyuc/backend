package com.guardias.backend.dto.asignacionHorasEfector;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HorasDisponiblesEfectorDto  {
    
    private BigDecimal horasAsignadas;   // cuota que Capital Humano le dio a ESTE efector ese mes
    private BigDecimal horasCargadas;    // lo que RRHH del efector ya cargó en distribuciones para esa persona+efector
    private BigDecimal horasDisponibles; // resta
}
