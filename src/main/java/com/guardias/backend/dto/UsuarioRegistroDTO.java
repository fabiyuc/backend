package com.guardias.backend.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRegistroDTO {

    @NotBlank
    private String usuario;
    @NotBlank
    private String contrasena;
    private boolean activo;
    @NotBlank
    private boolean estado;
    @Min(value = 1)
    private Long idTipoUsuario;
    @NotBlank
    private LocalDate fechaAlta;
    private LocalDate fechaUltimaSesion;
    private Long intentosFallidos;

}
