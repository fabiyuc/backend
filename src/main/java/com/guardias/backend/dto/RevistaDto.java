package com.guardias.backend.dto;

import com.guardias.backend.entity.CargaHoraria;
import com.guardias.backend.entity.TipoRevista;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RevistaDto {

    @NotBlank
    private TipoRevista tipoRevista;

    /*
     * @NotBlank
     * private Categoria categoria;
     */

    /*
     * @NotBlank
     * private Adicional adicional;
     */

    @NotBlank
    private CargaHoraria cargaHoraria;

}
