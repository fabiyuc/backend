package com.guardias.backend.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistribucionHorariaDto {

    @Min(value = 1)
    private int cantidad;
    private String descripcion;
}
