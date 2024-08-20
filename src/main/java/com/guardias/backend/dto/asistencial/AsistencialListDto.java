package com.guardias.backend.dto.asistencial;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsistencialListDto {
    
    private Long id;
    private String nombre;
    private String apellido;
    private int dni;
    private String cuil;
    private LocalDate fechaNacimiento;
    private String sexo;
    private String telefono;
    private String email;
    private String domicilio;
    private List<String> nombresTiposGuardias;
    /* private List<Long> idTiposGuardias; */
}
