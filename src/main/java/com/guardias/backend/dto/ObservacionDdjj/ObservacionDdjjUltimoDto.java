package com.guardias.backend.dto.ObservacionDdjj;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ObservacionDdjjUltimoDto {

    private Long id;
    private String motivo;
    private String nombreUsuario;
    private String apellidoUsuario;
    private LocalDate fechaCreacion;
    private LocalTime horaCreacion;

}
