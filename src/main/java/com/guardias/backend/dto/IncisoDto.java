package com.guardias.backend.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class IncisoDto extends LeyDto {

    private Long idInciso;
    private List<Long> idSubIncisos;
    private Long idArticulo;
}
