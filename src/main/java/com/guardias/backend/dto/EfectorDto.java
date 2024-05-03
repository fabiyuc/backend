package com.guardias.backend.dto;

import java.util.List;

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
    private boolean activo;

    private Long idLocalidad;
    private Long idRegion;
    private List<Long> idDistribucionesHorarias;
    private List<Long> idAutoridades;
    private List<Long> idLegajosUdo;
    private List<Long> idLegajos;
    private List<Long> idNotificaciones;
}
