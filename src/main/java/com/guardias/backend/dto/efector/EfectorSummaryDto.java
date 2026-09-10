package com.guardias.backend.dto.efector;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EfectorSummaryDto {

    private Long id;
    private String nombre;
    private String region;
    
    public EfectorSummaryDto(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public EfectorSummaryDto(Long id, String nombre, String region) {
        this.id = id;
        this.nombre = nombre;
        this.region = region;
    }
}
