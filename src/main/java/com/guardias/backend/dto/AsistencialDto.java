package com.guardias.backend.dto;

import java.util.HashSet;
import java.util.Set;
import com.guardias.backend.entity.Especialidad;
import com.guardias.backend.entity.Legajo;
import com.guardias.backend.entity.RegistroActividad;
import com.guardias.backend.enums.TipoGuardiaEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AsistencialDto extends PersonDto {

    @NotBlank
    private Set<Legajo> legajos;

    @NotBlank
    private TipoGuardiaEnum tipoGuardia;
    private Set<Especialidad> especialidades = new HashSet<>();
    Set<RegistroActividad> registrosActividades;

}
