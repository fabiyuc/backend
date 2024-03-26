package com.guardias.backend.dto;

import java.util.List;

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
    private Long idcategoria;
    @NotBlank
    private Long idadicional;

    @NotBlank
    private Long idcargaHoraria;
    private List<Long> idlegajos;

    @NotBlank
    private boolean activo;

    @NotNull
    private AgrupacionEnum agrupacion;

}
