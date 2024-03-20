package com.guardias.backend.dto;

import java.util.Set;

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
    private Long idLocalidad;
    private boolean activo;

    private Long idRegion;
    private Set<Long> idDistribucionesHorarias;
    private Set<Long> idAutoridades;
    private Set<Long> idLegajosUdo;
    private Set<Long> idLegajos;
    private Set<Long> idNotificaciones;
}
