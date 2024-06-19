package com.guardias.backend.dto;

import java.util.List;

import com.guardias.backend.enums.TipoGuardiaEnum;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoGuardiaDto {

    @NotBlank
    private TipoGuardiaEnum nombre;
    @NotBlank
    private String descripcion;
    private Boolean activo;
    private List<Long> idAsistenciales;
    private List<Long> idRegistrosActividades;
}