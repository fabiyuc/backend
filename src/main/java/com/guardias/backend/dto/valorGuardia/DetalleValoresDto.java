package com.guardias.backend.dto.valorGuardia;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleValoresDto {
    private ValorGuardiaResponseDto cargo;
    private ValorGuardiaResponseDto extra;
}
