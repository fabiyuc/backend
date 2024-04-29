package com.guardias.backend.dto;

import java.time.LocalDate;
import java.util.List;

import com.guardias.backend.enums.TipoNotificacionEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificacionDto {

    @NotNull
    private TipoNotificacionEnum tipo;

    @NotBlank
    private String categoria;

    @NotBlank
    private String detalle;

    @NotBlank
    private String url;

    @NotNull
    private LocalDate fechaNotificacion;

    @NotNull
    private LocalDate fechaBaja;

    @NotNull
    private boolean activo;

    @NotNull
    private List<Long> idEfectores;

}
