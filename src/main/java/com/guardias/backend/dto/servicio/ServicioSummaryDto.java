package com.guardias.backend.dto.servicio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicioSummaryDto {
    private Long id;
    private String descripcion;
}
