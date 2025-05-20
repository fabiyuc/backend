package com.guardias.backend.dto.cronogramaTentativo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerificacionTentativoResponseDto {

     private Long id;
     private boolean existe;
    
}
