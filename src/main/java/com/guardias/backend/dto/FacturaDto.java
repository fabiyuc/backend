package com.guardias.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacturaDto {
    
    private String contribuyente;

    private String tipo;

    private Long puntoVenta;

    private Long numeroFactura;

    private LocalDate fechaEmision;
    
    private BigDecimal monto;

    private boolean activo;

    private Long idAsistencial;
}
