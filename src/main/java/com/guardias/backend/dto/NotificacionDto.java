package com.guardias.backend.dto;

import java.time.LocalDate;
import com.guardias.backend.enums.TipoNotificacionEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificacionDto {

    @NotNull
    private TipoNotificacionEnum tipo;

    @NotBlank
    private String categoria;

    @NotNull
    private LocalDate fechaNotificacion;

    @NotBlank
    private String detalle;

    @NotBlank
    private String url;

    @NotNull
    private boolean activo;

    @NotNull
    private LocalDate fechaBaja;

}
