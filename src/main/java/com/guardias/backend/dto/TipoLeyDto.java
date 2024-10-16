package com.guardias.backend.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoLeyDto {

    @NotBlank
    private String descripcion;
    private boolean activo;
    List<Long> idLeyes;
    /* List<Long> idTipoLey; */
}
