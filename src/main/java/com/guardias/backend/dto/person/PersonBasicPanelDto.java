package com.guardias.backend.dto.person;

import java.util.List;

import com.guardias.backend.dto.efector.EfectorSummaryDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonBasicPanelDto {
    
    private String nombre;
    private String apellido;
    private EfectorSummaryDto udo;
    private List<EfectorSummaryDto> efectores;

}
