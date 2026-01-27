package com.guardias.backend.dto.valorGuardia;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColumnaGrillaDto {
    private String titulo; // "SUSQUES" o "RESTO"
    private DetalleValoresDto valores; // Adentro tiene "cargo" y "extra"
}
