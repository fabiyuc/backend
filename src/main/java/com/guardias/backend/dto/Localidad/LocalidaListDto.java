package com.guardias.backend.dto.Localidad;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocalidaListDto {

    private Long id;

    @NotBlank
    private String nombre;
}
