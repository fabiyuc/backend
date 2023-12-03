package com.guardias.backend.dto;

import java.util.Set;

import com.guardias.backend.entity.Adicional;
import com.guardias.backend.entity.CargaHoraria;
import com.guardias.backend.entity.Legajo;
import com.guardias.backend.entity.TipoRevista;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RevistaDto {

    @NotBlank
    private TipoRevista tipoRevista;

    @NotBlank
    private CargaHoraria cargaHoraria;
    private Adicional adicional;
    private Set<Legajo> legajos;

}
