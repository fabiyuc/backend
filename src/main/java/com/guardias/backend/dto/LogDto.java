package com.guardias.backend.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LogDto {

    @NotBlank
    private LocalDateTime fecha;
    @NotBlank
    private String seccion;
    @NotBlank
    private String accion;

}
