package com.guardias.backend.dto;

import java.util.Set;

import com.guardias.backend.entity.Especialidad;
import com.guardias.backend.entity.Legajo;
import com.guardias.backend.entity.TipoGuardia;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AsistencialDto extends PersonDto {

    private Set<Legajo> legajos;
    private TipoGuardia tipoGuardia;
    private Especialidad especialidad;

}
