package com.guardias.backend.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegionDto {

    @NotBlank
    private String nombre;
    private List<Long> idEfectores;
    private boolean activo;
}
