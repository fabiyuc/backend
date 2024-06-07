package com.guardias.backend.dto;

import java.math.BigDecimal;

import com.guardias.backend.entity.RegistroActividad;
import com.guardias.backend.enums.TipoDiaEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SumaHorasDto {

    private float cantidadHoras;
    private TipoDiaEnum tipoDia; // finde - feriado - bono o normal
    private BigDecimal montoHora;
    private BigDecimal totalAPagar;
    private RegistroActividad registroActividad;
    private boolean activo;
}
