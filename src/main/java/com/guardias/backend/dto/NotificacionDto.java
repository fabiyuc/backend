package com.guardias.backend.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

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
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fNotificacion;
    @NotBlank
    private String detalle;
    @NotBlank
    private String url;

}
