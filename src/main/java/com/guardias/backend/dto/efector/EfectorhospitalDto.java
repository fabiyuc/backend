package com.guardias.backend.dto.efector;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class EfectorhospitalDto {

    private Long id;
    private String nombre;
    @NotBlank
    @Min(value = 1)
    private Long nivelComplejidad;

}
