package com.guardias.backend.dto;

import com.guardias.backend.entity.Localidad;
import com.guardias.backend.entity.Region;

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
    private Region region;
    private Localidad localidad;
}
