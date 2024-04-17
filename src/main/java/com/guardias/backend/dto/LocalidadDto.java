package com.guardias.backend.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocalidadDto {

    @NotBlank
    private String nombre;

    Long idDepartamento;
    private boolean activo;

    private List<Long> idEfectores;

}
