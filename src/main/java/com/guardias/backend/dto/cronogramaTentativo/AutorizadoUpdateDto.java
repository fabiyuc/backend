package com.guardias.backend.dto.cronogramaTentativo;

import com.guardias.backend.enums.AutorizadoTentativoEnum;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AutorizadoUpdateDto {

    @NotNull
    private AutorizadoTentativoEnum autorizado;
    private String motivoAutorizacion;
    private String motivoPendiente;
    private Long idAutoridad;

}
