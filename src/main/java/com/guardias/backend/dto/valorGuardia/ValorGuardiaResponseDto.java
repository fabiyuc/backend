package com.guardias.backend.dto.valorGuardia;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValorGuardiaResponseDto {
    // --- FILAS EXCLUSIVAS DE GUARDIA CARGO ---
    // (Vendrán en null si es Guardia Extra)
    private MontoDto decreto1178;
    private MontoDto decreto1657;

    // --- FILAS EXCLUSIVAS DE GUARDIA EXTRA ---
    // (Vendrán en null si es Guardia Cargo)
    private MontoDto resolucion2575;

    // Fila "BONO DECR Nº 1580":
    // Es el valor monetario calculado (lo que guardamos como valorBonoUtiLav/Sdf)
    private MontoDto bono1580; 

    // Fila "TOTAL":
    // El valor final que ya tienes en base de datos
    private MontoDto total;
}
