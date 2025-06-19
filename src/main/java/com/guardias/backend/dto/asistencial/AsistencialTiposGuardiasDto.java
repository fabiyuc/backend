package com.guardias.backend.dto.asistencial;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsistencialTiposGuardiasDto {
    private Long idTipoGuardia;
    private String nombreTipoGuardia;
}
