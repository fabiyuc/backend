package com.guardias.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CapsDto extends EfectorDto {

    @Min(value = 1)
    private Long idUdo;

    @NotBlank
    private String tipoCaps;

}
