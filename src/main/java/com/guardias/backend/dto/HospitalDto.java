package com.guardias.backend.dto;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class HospitalDto extends EfectorDto {
    
    @NotBlank
    private Boolean esCabecera;
    
    @NotBlank
    private Boolean admitePasiva;
    
    @NotBlank
    @Min(value = 1)
    private Long nivelComplejidad;

    List<Long> idCaps;

}
