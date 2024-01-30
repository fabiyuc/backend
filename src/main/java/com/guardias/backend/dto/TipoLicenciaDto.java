package com.guardias.backend.dto;

import com.guardias.backend.entity.NovedadPersonal;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoLicenciaDto {

    @NotBlank
    private String nombre;

    @NotBlank
    private String ley;

    private String articulo;

    private String inciso;

}
