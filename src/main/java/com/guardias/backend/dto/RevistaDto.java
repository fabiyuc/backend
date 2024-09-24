package com.guardias.backend.dto;

import java.util.List;

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
    private Long idTipoRevista;

    @NotBlank
    private Long idCategoria;
    @NotNull
    private Long idAdicional;

    @NotBlank
    private Long idCargaHoraria;
    private List<Long> idLegajos;
    @NotNull
    private String agrupacion; // Cambiado a String para el displayName

}
