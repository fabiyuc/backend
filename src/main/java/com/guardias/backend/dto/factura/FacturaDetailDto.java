package com.guardias.backend.dto.factura;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.guardias.backend.enums.CondicionFiscalEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacturaDetailDto {
    
    private Long id;
    private String nombreTitular;
    private String apellidoTitular;
    private int dniTitular;
    private String cuilTitular;
    private CondicionFiscalEnum contribuyente;
    private String tipo;
    private Long puntoVenta;
    private Long numeroFactura;
    private LocalDate fechaEmision;
    private BigDecimal monto;
    
}
