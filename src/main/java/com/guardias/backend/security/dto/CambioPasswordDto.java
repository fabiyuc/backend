package com.guardias.backend.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CambioPasswordDto {

    @NotBlank
    private String nombreUsuario;
    
    @NotBlank
    private String passwordActual;
    
    @NotBlank
    @Size(min = 4, message = "La nueva contraseña debe tener al menos 4 caracteres")
    private String nuevaPassword;
    
    @NotBlank
    private String confirmacionPassword;
}
