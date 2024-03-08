package com.guardias.backend.dto;

import java.util.List;
import com.guardias.backend.entity.Caps;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class HospitalDto extends EfectorDto {

    private boolean esCabecera;
    private boolean admitePasiva;

    @Min(value = 1)
    private int nivelComplejidad;

    List<Caps> caps;

}
