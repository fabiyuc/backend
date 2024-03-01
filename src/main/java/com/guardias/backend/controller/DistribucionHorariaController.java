package com.guardias.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.guardias.backend.dto.DistribucionHorariaDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.DistribucionHoraria;
import com.guardias.backend.service.DistribucionHorariaService;

@RestController
public class DistribucionHorariaController {
    @Autowired
    DistribucionHorariaService distribucionHorariaService;

    public ResponseEntity<?> validations(DistribucionHorariaDto distribucionHorariaDto) {
        if (distribucionHorariaDto.getDia() == null)
            return new ResponseEntity(new Mensaje("El dia es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionHorariaDto.getFechaInicio() == null)
            return new ResponseEntity(new Mensaje("la fecha de inicio es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionHorariaDto.getHoraIngreso() == null)
            return new ResponseEntity(new Mensaje("la hora de ingreso es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionHorariaDto.getCantidadHoras() == null)
            return new ResponseEntity(new Mensaje("la cantidad es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionHorariaDto.getEfector() == null)
            return new ResponseEntity(new Mensaje("El efector es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionHorariaDto.getPersona() == null)
            return new ResponseEntity(new Mensaje("la persona es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    public DistribucionHoraria createUpdate(DistribucionHoraria distribucionHoraria,
            DistribucionHorariaDto distribucionHorariaDto) {
        if (!distribucionHorariaDto.getDia().equals(distribucionHoraria.getDia()))
            distribucionHoraria.setDia(distribucionHorariaDto.getDia());
        if (!distribucionHorariaDto.getFechaInicio().equals(distribucionHoraria.getFechaInicio()))
            distribucionHoraria.setFechaInicio(distribucionHorariaDto.getFechaInicio());
        if (!distribucionHorariaDto.getFechaFinalizacion().equals(distribucionHoraria.getFechaFinalizacion()))
            distribucionHoraria.setFechaFinalizacion(distribucionHorariaDto.getFechaFinalizacion());
        if (!distribucionHorariaDto.getHoraIngreso().equals(distribucionHoraria.getHoraIngreso()))
            distribucionHoraria.setHoraIngreso(distribucionHorariaDto.getHoraIngreso());
        if (!distribucionHorariaDto.getPersona().equals(distribucionHoraria.getPersona()))
            distribucionHoraria.setPersona(distribucionHorariaDto.getPersona());
        if (!distribucionHorariaDto.getEfector().equals(distribucionHoraria.getEfector()))
            distribucionHoraria.setEfector(distribucionHorariaDto.getEfector());
        if (!distribucionHorariaDto.getCantidadHoras().equals(distribucionHoraria.getCantidadHoras()))
            distribucionHoraria.setCantidadHoras(distribucionHorariaDto.getCantidadHoras());
        distribucionHoraria.setActivo(distribucionHorariaDto.isActivo());
        return distribucionHoraria;
    }
}
