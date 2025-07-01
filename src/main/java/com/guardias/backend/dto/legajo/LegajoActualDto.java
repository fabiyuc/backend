package com.guardias.backend.dto.legajo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LegajoActualDto {
    private String nombreAdicional;
    private String nombreCategoria;
    private String nombreTipoRevista;
}