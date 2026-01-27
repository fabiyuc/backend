package com.guardias.backend.dto.valorGuardia;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrillaValorGuardiaCompletaDto {

    private String nombreNivel; // Ej: "NIVEL 1"
    private int numeroNivel;    // Para ordenar (1, 2, 3, 4)
    private List<ColumnaGrillaDto> columnas = new ArrayList<>();
    
}
