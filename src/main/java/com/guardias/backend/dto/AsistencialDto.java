package com.guardias.backend.dto;

import java.util.Set;

import com.guardias.backend.entity.Legajo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsistencialDto extends PersonDto {

    private Set<Legajo> legajos;

}
