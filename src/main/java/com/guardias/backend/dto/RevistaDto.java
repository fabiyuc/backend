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

    private boolean activo;
    @NotBlank
    private Long idTipoRevista;
    @NotBlank
    private Long idCategoria;
    @NotBlank
    private Long idAdicional;
    @NotBlank
    private Long idCargaHoraria;
    @NotNull
    private AgrupacionEnum agrupacion;
    private List<Long> idLegajos;
    

}
