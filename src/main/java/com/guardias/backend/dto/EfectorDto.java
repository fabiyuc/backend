package com.guardias.backend.dto;

import java.util.Set;

import com.guardias.backend.entity.Autoridad;
import com.guardias.backend.entity.DistribucionHoraria;
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

    private Set<DistribucionHoraria> distribucionesHorarias;

    private Set<Autoridad> autoridades;
}
