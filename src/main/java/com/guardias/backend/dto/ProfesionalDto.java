package com.guardias.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfesionalDto { // !! VER si hereda de algun lado (FABI: NO ES NECESARIO, PERO SI SE
    // TIENE QUE INDICAR TODOS LOS ATRIBUTOS QUE HEREDA, AQUI FALTAN PORQUE SOLO SE USO UNOS PARA PRUEBA)

    @NotBlank
    private Long id;

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    @NotBlank
    private String dni;

}
