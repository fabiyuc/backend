package com.guardias.backend.dto.departamento;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartamentoListDto {

    private Long id;

    @NotBlank
    private String nombre;

}
