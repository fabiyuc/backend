package com.guardias.backend.dto.valorGuardia;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrillaVigenteDto {
    private LocalDate fechaInicio;   // desde cuándo rige la grilla; null si no hay ninguna generada
    private List<GrillaValorGuardiaCompletaDto> niveles = new ArrayList<>();
}
