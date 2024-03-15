package com.guardias.backend.dto;

import java.util.ArrayList;
import java.util.List;
import com.guardias.backend.entity.Efector;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegionDto {

    @NotBlank
    private String nombre;
    private List<Efector> efectores = new ArrayList<Efector>();
    private boolean activo;
}
