package com.guardias.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Mensaje {
    private String mensaje;
    private boolean activo;

}
