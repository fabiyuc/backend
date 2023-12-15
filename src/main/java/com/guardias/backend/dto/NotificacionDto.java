package com.guardias.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificacionDto {

    @NotBlank
    private String tipo;
    @NotBlank
    private Long posicion;
    @NotBlank
    private String categoria;
    @NotBlank
    private String fechanotificacion;
    @NotBlank
    private String detalle;
    @NotBlank
    private String url;

}
