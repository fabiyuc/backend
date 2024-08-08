package com.guardias.backend.dto.profesion;

import java.util.List;
import com.guardias.backend.dto.especialidad.EspecialidadSummaryDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfesionSummaryDto {
     private Long id;
    private String nombre;
    private List<EspecialidadSummaryDto> nombresEspecialidades;
}
