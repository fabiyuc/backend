package com.guardias.backend.dto;

import java.util.List;

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
    /*
     * @NotBlank
     * private String ley;
     */

    private boolean activo;
    private List<Long> idArticulos;
    private List<Long> idIncisos;
    private Long idTipoLey;

    private List<Long> idNovedadesPersonales;

}
