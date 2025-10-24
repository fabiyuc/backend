package com.guardias.backend.dto.region;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegionSummaryDto {

    @NotBlank
    private Long id;
    private String nombre;
    private boolean activo;
}
