package com.guardias.backend.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProvinciaDTO {

    @NotBlank
    private String nombre;
    private String gentilicio;
    private boolean activo;
    private Long idPais;
    private List<Long> idDepartamentos;

}
