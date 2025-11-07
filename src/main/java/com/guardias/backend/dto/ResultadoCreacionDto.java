package com.guardias.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultadoCreacionDto {
    
    private int distribucionesCreadas;
    private int cronogramasCreados; 
}
