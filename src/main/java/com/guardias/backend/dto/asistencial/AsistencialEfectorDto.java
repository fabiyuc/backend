package com.guardias.backend.dto.asistencial;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsistencialEfectorDto {

    private Long id;
    private String nombre;
    private String apellido;
    private String cuil;
    private boolean activo;

}