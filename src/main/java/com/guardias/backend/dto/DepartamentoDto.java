package com.guardias.backend.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartamentoDto {

    @NotBlank
    private String nombre;
    private String codigoPostal;
    private boolean activo;
    private Long idProvincia;
    private List<Long> idLocalidades;

}
