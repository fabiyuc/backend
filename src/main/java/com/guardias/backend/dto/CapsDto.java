package com.guardias.backend.dto;

import com.guardias.backend.entity.Hospital;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CapsDto extends EfectorDto {

    Hospital cabecera;

    @NotBlank
    private String tipoCaps;

}
