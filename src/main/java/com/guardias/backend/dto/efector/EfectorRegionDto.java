package com.guardias.backend.dto.efector;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class EfectorRegionDto {
    private Long id;
    private String nombre;
    private Long idRegion;
}
