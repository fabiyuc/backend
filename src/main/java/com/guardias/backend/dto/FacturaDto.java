package com.guardias.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.guardias.backend.enums.CondicionFiscalEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacturaDto {
    
    private Long idAsistencial;

    private List<Long> idRegistrosMensuales;

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

    private boolean activo;

    
}
