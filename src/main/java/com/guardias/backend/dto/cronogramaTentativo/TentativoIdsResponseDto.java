package com.guardias.backend.dto.cronogramaTentativo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TentativoIdsResponseDto {
    
    private Long idServicio;
    private Long idTipoGuardia;
}
