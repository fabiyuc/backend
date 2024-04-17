package com.guardias.backend.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class IncisoDto extends LeyDto {

    @NotNull
    private Long idInciso;
    private List<Long> idSubIncisos;
    @NotNull
    private Long idArticulo;
    private List<Long> idNovedadesPersonales;
}
