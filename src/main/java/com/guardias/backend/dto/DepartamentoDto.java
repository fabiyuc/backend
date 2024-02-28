package com.guardias.backend.dto;

import java.util.List;
import com.guardias.backend.entity.Localidad;
import com.guardias.backend.entity.Provincia;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DepartamentoDto {

    @NotBlank
    private String nombre;
    private String codigoPostal;
    private boolean activo;
    Provincia provincia;
    List<Localidad> localidades;

}
