package com.guardias.backend.dto;

import java.util.Set;

import com.guardias.backend.entity.Revista;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdicionalDto {

    @NotBlank
    private String nombre;
    private Set<Revista> revistas;
    private boolean activo;
}
