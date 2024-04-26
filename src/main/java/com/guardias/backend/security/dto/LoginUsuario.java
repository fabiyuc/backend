package com.guardias.backend.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUsuario {
    
    @NotBlank
    private String nombreUsuario;
    @NotBlank
    private String password;
    
}
