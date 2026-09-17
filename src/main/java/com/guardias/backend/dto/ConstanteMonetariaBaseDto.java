package com.guardias.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.guardias.backend.enums.FamiliaValorBaseEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConstanteMonetariaBaseDto {

    private boolean activo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private BigDecimal monto;
    //private TipoGuardiaEnum tipoGuardia;
    private FamiliaValorBaseEnum familiaValorBase;
    private String documentoLegal;
    //private List<Long> idDdjjs;
}
