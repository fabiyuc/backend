package com.guardias.backend.dto;

import java.util.List;

import com.guardias.backend.enums.MesesEnum;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CronogramaDefinitivoDto {
    
     @NotBlank
    private MesesEnum mes;
    @Min(value = 1991)
    private int anio;

    private boolean activo;

    private Long idEfector;

    private List<Long> idDdjjs;
    
}
