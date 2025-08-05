package com.guardias.backend.dto.legajo;

import com.guardias.backend.dto.revista.RevistaListDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LegajoListDto {
    private RevistaListDto revista;
}
