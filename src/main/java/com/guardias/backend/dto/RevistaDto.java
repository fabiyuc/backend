package com.guardias.backend.dto;

import java.util.List;

import com.guardias.backend.entity.Legajo;
import com.guardias.backend.enums.AgrupacionEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevistaDto {

    @NotBlank
    private Long idtipoRevista;

    @NotBlank
    private Long idcategoria;
    private boolean activo;

    @NotBlank
    private Long idadicional;

    @NotBlank
    private Long idcargaHoraria;
    private List<Legajo> idlegajos;
    @NotNull
    private AgrupacionEnum agrupacion;

}
