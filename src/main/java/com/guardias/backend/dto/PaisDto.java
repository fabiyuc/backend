package com.guardias.backend.dto;

import java.util.List;
import com.guardias.backend.entity.Provincia;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaisDto {

    @NotBlank
    private String nombre;
    @NotBlank
    private String nacionalidad;
    private String codigo;
    private boolean activo;
    List<Provincia> provincias;

}
