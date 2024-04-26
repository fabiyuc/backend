package com.guardias.backend.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EspecialidadDto {
    @NotBlank
    private String nombre;
    private Long idProfesion;
    private Boolean esPasiva;
    private boolean activo;
    private List<Long> idAsistenciales;

}
