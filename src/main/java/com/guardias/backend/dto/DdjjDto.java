package com.guardias.backend.dto;

import java.math.BigDecimal;
import java.util.List;

import com.guardias.backend.enums.EstadoDdjjEnum;
import com.guardias.backend.enums.MesesEnum;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DdjjDto {

    @NotBlank
    private MesesEnum mes;
    @Min(value = 1991)
    private int anio;
    @Min(value = 1)
    private boolean activo;
    private BigDecimal subtotal;
    private BigDecimal total;
    @NotBlank
    private EstadoDdjjEnum estadoDdjj;
    private Long idValorGmi;
    private Long idEfector;
    List<Long> idRegistrosMensuales;
}
