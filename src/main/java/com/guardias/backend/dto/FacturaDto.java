package com.guardias.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.guardias.backend.enums.CondicionFiscalEnum;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacturaDto {
    
    @NotBlank
    private Long idAsistencial;

    @NotBlank
    private List<Long> idRegistrosMensuales;

    @NotBlank
    private String nombreTitular;

    @NotBlank
    private String apellidoTitular;

    @NotBlank
    private int dniTitular;

    @NotBlank
    private String cuilTitular;

    @NotBlank
    private CondicionFiscalEnum contribuyente;

    @NotBlank
    private String tipo;

    @NotBlank
    private Long puntoVenta;

    @NotBlank
    private Long numeroFactura;

    @NotBlank
    private LocalDate fechaEmision;
    
    @NotBlank
    private BigDecimal monto;

    private boolean activo;

}
