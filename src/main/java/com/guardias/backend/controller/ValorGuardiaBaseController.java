package com.guardias.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.ValorGuardiaBaseDto;
import com.guardias.backend.service.ValorGuardiaBaseService;

@RestController
public class ValorGuardiaBaseController {

    @Autowired
    ValorGuardiaBaseService valorGuardiaBaseService;

    public ResponseEntity<?> validations(ValorGuardiaBaseDto valorGuardiaBaseDto, Long id) {

        if (valorGuardiaBaseDto.getTipoGuardia() == null)
            return new ResponseEntity(new Mensaje("El tipo de guardia es obligatorio"), HttpStatus.BAD_REQUEST);

        if (valorGuardiaBaseDto.getNivelComplejidad() <= 0)
            return new ResponseEntity(new Mensaje("El tipo de guardia es obligatorio"), HttpStatus.BAD_REQUEST);

        if (valorGuardiaBaseDto.getFechaInicio() == null)
            return new ResponseEntity(new Mensaje("la fecha de inicio es obligatoria"), HttpStatus.BAD_REQUEST);

        // el valor del gmi y bono uti se tomaran al momento de guardar

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }
}
