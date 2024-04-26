package com.guardias.backend.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaisDto {

    @NotBlank
    private String nombre;
    @NotBlank
    private String nacionalidad;
    private String codigo;
    private boolean activo;
    List<Long> idProvincias;

}
