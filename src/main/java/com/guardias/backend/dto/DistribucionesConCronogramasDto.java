package com.guardias.backend.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistribucionesConCronogramasDto {
    
    private List<DistribucionGuardiaDto> guardias;
    private List<DistribucionConsultorioDto> consultorios;
    private List<DistribucionGiraDto> giras;
    private List<DistribucionOtraDto> otras;
    private boolean crearCronogramasParaGuardias = true;
}
