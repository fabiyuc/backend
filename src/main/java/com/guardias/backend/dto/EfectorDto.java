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

    private Long id;

    @NotBlank
    private String nombre;

    @NotBlank
    private String domicilio;

    private String telefono;

    private boolean estado;

    private boolean activo;

    private String observacion;

    @NotBlank
    private Long idRegion;

    @NotBlank
    private Long idLocalidad;

    private List<Long> idDistribucionesHorarias;
    private List<Long> idLegajosUdo;
    private List<Long> idLegajos;
    private List<Long> idServicios;
    private List<Long> idNotificaciones;
    private List<Long> idRegistrosActividades;
    private List<Long> idRegistroMensual;
    private List<Long> idDdjjs;
    private List<Long> idRegistrosPendientes;
    private List<Long> idHabilitacionesGuardias;
    private List<Long> idHabilitacionesGenerales;
    private List<Long> idCronogramasTentativos;
    private List<Long> idFeriados;

}
