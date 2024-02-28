package com.guardias.backend.dto;

import java.util.Set;
import com.guardias.backend.entity.Legajo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class NoAsistencialDto extends PersonDto {

    private String descripcion;
    private Set<Legajo> legajos;

}
