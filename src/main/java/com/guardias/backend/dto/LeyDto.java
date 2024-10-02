package com.guardias.backend.dto;

import java.time.LocalDate;

import com.guardias.backend.enums.EstadoLey;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeyDto {
    @NotEmpty
    private String numero; // obligatorio y no vacío para articulo e inciso
    @NotEmpty
    private String denominacion; // obligatorio y no vacío para articulo e inciso
    @NotEmpty
    private String detalle; // obligatorio y no vacío para articulo e inciso
    @NotEmpty
    private EstadoLey estado; // obligatorio y no vacío para articulo e inciso

    private LocalDate fechaAlta;
    private LocalDate fechaBaja;
    private LocalDate fechaModificacion;
    private String motivoModificacion;
    private boolean activo;
    @NotNull
    Long idTipoLey; // obligatorio y no vacío para articulo e inciso
}
