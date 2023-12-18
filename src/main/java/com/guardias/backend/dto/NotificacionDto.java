package com.guardias.backend.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificacionDto {

    @NotBlank
    private String tipo;
    @NotBlank
    private String categoria;
    @NotNull
    private LocalDate fechaNotificacion;
    @NotBlank
    private String detalle;
    @NotBlank
    private String url;

}
