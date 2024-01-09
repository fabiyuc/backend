package com.guardias.backend.dto;

import java.time.LocalDate;

import com.guardias.backend.entity.Asistencial;
import com.guardias.backend.entity.NoAsistencial;
import com.guardias.backend.entity.TipoLicencia;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NovedadPersonalDto {

    @NotNull
    private LocalDate fechaInicio;
    private LocalDate fechaFinal;
    private boolean puedeRealizarGuardia;
    private boolean cobraSueldo;
    private boolean necesitaReemplazo;
    private boolean activa;
    private String descripcion;
    private Long idExtensionLicencia;

    @NotEmpty
    private Asistencial novedadesAsistencial;
    private Asistencial asistencialReemplazante;
    private NoAsistencial novedadesNoAsistencial;
    private NoAsistencial reemplazantesNoAsistencial;

    @NotEmpty
    private TipoLicencia tipoLicencia;
}
