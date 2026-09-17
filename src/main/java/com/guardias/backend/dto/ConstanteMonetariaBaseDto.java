package com.guardias.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
    private String familiaValorBase;
    private String documentoLegal;
    private List<Long> idDdjjs;
}
