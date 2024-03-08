package com.guardias.backend.dto;

import java.time.LocalDate;
import com.guardias.backend.entity.TipoLey;
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
    String numero;
    @NotEmpty
    String denominacion;
    String detalle;
    @NotEmpty
    EstadoLey estado;
    @NotEmpty
    LocalDate fechaAlta;
    LocalDate fechaBaja;
    LocalDate fechaModificacion;
    String motivoModificacion;
    private boolean activo;

    @NotNull
    TipoLey ley;
}
