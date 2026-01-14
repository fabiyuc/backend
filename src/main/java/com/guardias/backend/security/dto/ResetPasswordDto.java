package com.guardias.backend.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordDto {
    @NotBlank
    private String nombreUsuario;
    
    @NotBlank
    @Size(min = 4)
    private String nuevaPassword;
}
