package com.guardias.backend.dto;

import java.time.LocalDate;
import java.util.Set;

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
    private String numero;
    @NotEmpty
    private String denominacion;
    private String detalle;
    @NotEmpty
    private EstadoLey estado;
    @NotEmpty
    private LocalDate fechaAlta;
    private LocalDate fechaBaja;
    private LocalDate fechaModificacion;
    private String motivoModificacion;
    private boolean activo;
    private Set<Long> idNovedadesPersonales;

    @NotNull
    Long idTipoLey;
}
