package com.guardias.backend.dto;

import java.util.List;

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

    @NotNull
    private boolean activo;
    List<Long> idRevistas;
}