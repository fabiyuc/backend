package com.guardias.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.guardias.backend.enums.TipoGuardiaEnum;
import com.guardias.backend.enums.TipoGuardiaGmi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValorGmiDto {

    private boolean activo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private BigDecimal monto;
    private TipoGuardiaGmi tipoGuardia;
    private String documentoLegal;
    private List<Long> idDdjjs;
}
