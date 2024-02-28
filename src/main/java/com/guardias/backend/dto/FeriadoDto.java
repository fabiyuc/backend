package com.guardias.backend.dto;

import java.time.LocalDate;
import com.guardias.backend.enums.TipoFeriadoEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FeriadoDto {
    @NotNull
    private LocalDate fecha;
    @NotBlank
    private String motivo;
    @NotBlank
    private TipoFeriadoEnum tipoFeriado;
    private String descripcion;
    private boolean activo;
}
