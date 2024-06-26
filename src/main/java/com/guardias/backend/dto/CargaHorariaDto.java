package com.guardias.backend.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CargaHorariaDto {

    @NotBlank
    private int cantidad;
    private String descripcion;
    List<Long> idRevistas;
    private boolean activo;

}
