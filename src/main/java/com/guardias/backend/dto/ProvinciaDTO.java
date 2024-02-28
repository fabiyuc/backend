package com.guardias.backend.dto;

import java.util.List;
import com.guardias.backend.entity.Departamento;
import com.guardias.backend.entity.Pais;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProvinciaDTO {

    @NotBlank
    private String nombre;
    private String gentilicio;
    private boolean activo;
    Pais pais;
    List<Departamento> departamentos;

}
