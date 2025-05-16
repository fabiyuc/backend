package com.guardias.backend.dto.asistencial;

import java.util.List;

import com.guardias.backend.dto.PersonDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AsistencialListNombreTGDto extends PersonDto {

    private List<Long> idLegajos;
    private List<Long> idRegistrosActividades;
    private List<String> nombresTiposGuardias;
}
