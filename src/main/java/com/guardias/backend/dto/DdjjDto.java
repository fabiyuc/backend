package com.guardias.backend.dto;

import java.math.BigDecimal;

import com.guardias.backend.enums.EstadoDdjjEenum;
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
    private EstadoDdjjEenum estadoDdjj;
    private Long idValorGmi;
}
