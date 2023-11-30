package com.guardias.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EfectorDto {

    @NotBlank
    private String nombre;

    @NotBlank
    private String domicilio;
    private String telefono;
    @NotBlank
    private boolean estado;
    private String observacion;
    private Long idRegion;
    private Long idLocalidad;
}
