package com.guardias.backend.dto.especialidad;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EspecialidadSummaryDto {
    private Long id;
    private String nombre;
}
