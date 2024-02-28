package com.guardias.backend.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.guardias.backend.dto.EfectorDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Efector;
import com.guardias.backend.service.EfectorService;

@RestController
public class EfectorController {

    @Autowired
    EfectorService efectorService;

    public ResponseEntity<?> validations(EfectorDto efectorDto) {
        if (StringUtils.isBlank(efectorDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"),
                    HttpStatus.BAD_REQUEST);
        if (StringUtils.isBlank(efectorDto.getDomicilio()))
            return new ResponseEntity(new Mensaje("el domicilio es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        if (efectorService.existsByName(efectorDto.getNombre()))
            return new ResponseEntity(new Mensaje("ese nombre ya existe"),
                    HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    public Efector createUpdate(Efector efector, EfectorDto efectorDto) {

        if (!efectorDto.getNombre().equals(efector.getNombre()))
            efector.setNombre(efectorDto.getNombre());

        if (!efectorDto.getDomicilio().equals(efector.getDomicilio()))
            efector.setDomicilio(efectorDto.getDomicilio());

        if (!efectorDto.getTelefono().equals(efector.getTelefono()))
            efector.setTelefono(efectorDto.getTelefono());

        efector.setEstado(efectorDto.isEstado());

        if (!efectorDto.getRegion().equals(efector.getRegion()))
            efector.setRegion(efectorDto.getRegion());

        if (!efectorDto.getLocalidad().equals(efector.getLocalidad()))
            efector.setLocalidad(efectorDto.getLocalidad());

        if (!efectorDto.getObservacion().equals(efector.getObservacion()))
            efector.setObservacion(efectorDto.getObservacion());

        return efector;
    }

}
