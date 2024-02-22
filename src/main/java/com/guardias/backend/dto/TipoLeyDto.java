package com.guardias.backend.dto;

import java.util.List;

import com.guardias.backend.entity.Ley;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoLeyDto {

    @NotBlank
    String descripcion;
    private boolean activo;

    List<Ley> leyes;
}
