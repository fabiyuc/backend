package com.guardias.backend.dto.tipoGuardia;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoGuardiaListDto {
    
    private Long id;
    private String nombre;
}
