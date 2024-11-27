package com.guardias.backend.dto.legajo;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LegajoBajaDto  {
     
    private LocalDate fechaFinal;
    @NotBlank
    private String motivoBaja;
   
}
