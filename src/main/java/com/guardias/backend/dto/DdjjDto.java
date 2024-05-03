package com.guardias.backend.dto;

import java.math.BigDecimal;
import java.util.List;

import com.guardias.backend.enums.EstadoDdjjEnum;
import com.guardias.backend.enums.MesesEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DdjjDto {
    private boolean activo;
    private MesesEnum mes;
    private int anio;
    private BigDecimal subtotal;
    private BigDecimal total;
    private EstadoDdjjEnum estadoDdjj;
    private Long idValorGmi;
    private Long idEfector;
    List<Long> idRegistrosMensuales;
}
