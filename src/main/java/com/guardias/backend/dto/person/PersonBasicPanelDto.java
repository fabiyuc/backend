package com.guardias.backend.dto.person;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonBasicPanelDto {
    
    private String nombre;
    private String apellido;
    private String nombreUdo;
    private List<String> nombresEfectores;

}
