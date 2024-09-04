package com.guardias.backend.dto;

import com.guardias.backend.enums.TipoGuardiaEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DistribucionGuardiaDto extends DistribucionHorariaDto {
    
    private TipoGuardiaEnum tipoGuardia;

    private Long idServicio;
}
