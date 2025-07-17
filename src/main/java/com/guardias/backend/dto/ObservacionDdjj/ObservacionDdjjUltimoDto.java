package com.guardias.backend.dto.ObservacionDdjj;

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

}
