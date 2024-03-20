package com.guardias.backend.dto;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdicionalDto {

    @NotBlank
    private String nombre;
    Set<Long> idRevistas;
    @NotNull
    private boolean activo;
}