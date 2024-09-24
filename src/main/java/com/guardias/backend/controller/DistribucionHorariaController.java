package com.guardias.backend.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.guardias.backend.dto.DistribucionHorariaDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.DistribucionHoraria;
import com.guardias.backend.entity.Efector;
import com.guardias.backend.entity.Legajo;
import com.guardias.backend.service.DistribucionHorariaService;
import com.guardias.backend.service.EfectorService;
import com.guardias.backend.service.PersonService;

@RestController
public class DistribucionHorariaController {
    @Autowired
    DistribucionHorariaService distribucionHorariaService;
    @Autowired
    EfectorService efectorService;
    @Autowired
    PersonService personService;

    public ResponseEntity<?> validations(DistribucionHorariaDto distribucionHorariaDto) {
        if (distribucionHorariaDto.getDia() == null)
            return new ResponseEntity(new Mensaje("El dia es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionHorariaDto.getCantidadHoras() == null)
            return new ResponseEntity(new Mensaje("la cantidad es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionHorariaDto.getIdPersona() == null)
            return new ResponseEntity(new Mensaje("la persona es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionHorariaDto.getIdEfector() == null)
            return new ResponseEntity(new Mensaje("El efector es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionHorariaDto.getFechaInicio() == null)
            return new ResponseEntity(new Mensaje("la fecha de inicio es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionHorariaDto.getHoraIngreso() == null)
            return new ResponseEntity(new Mensaje("la hora de ingreso es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    public DistribucionHoraria createUpdate(DistribucionHoraria distribucionHoraria,
            DistribucionHorariaDto distribucionHorariaDto) {
        if (distribucionHorariaDto.getDia() != distribucionHoraria.getDia() && distribucionHorariaDto.getDia() != null)
            distribucionHoraria.setDia(distribucionHorariaDto.getDia());

        if (distribucionHorariaDto.getCantidadHoras() != distribucionHoraria.getCantidadHoras()
                && distribucionHorariaDto.getCantidadHoras() != null)
            distribucionHoraria.setCantidadHoras(distribucionHorariaDto.getCantidadHoras());

        distribucionHoraria.setActivo(distribucionHorariaDto.isActivo());

        if (distribucionHoraria.getPersona() == null ||
                (distribucionHorariaDto.getIdPersona() != null &&
                        !Objects.equals(distribucionHoraria.getPersona().getId(),
                                distribucionHorariaDto.getIdPersona()))) {
            distribucionHoraria.setPersona(personService.findById(distribucionHorariaDto.getIdPersona()));
        }

        // Verifica si la persona tiene legajos y si el UDO del legajo coincide con el Efector del DTO
        if (distribucionHoraria.getPersona() != null && distribucionHoraria.getPersona().getLegajos() != null) {
            Legajo legajoActual = distribucionHoraria.getPersona().getLegajos().stream()
                    .filter(Legajo::getActual) // Filtrar el legajo actual
                    .findFirst()
                    .orElse(null);

            if (legajoActual != null && legajoActual.getUdo() != null) {
                Efector udoPersona = legajoActual.getUdo();
                Efector efectorDto = efectorService.findById(distribucionHorariaDto.getIdEfector());

                // Compara el UDO de la persona con el Efector del DTO antes de asignar
                if (Objects.equals(udoPersona.getId(), efectorDto.getId())) {
                    distribucionHoraria.setEfector(efectorDto);
                } else {
                    throw new IllegalArgumentException("El Efector del DTO no coincide con el UDO de la persona.");
                }

            }
        }

        /* if (distribucionHoraria.getEfector() == null ||
                (distribucionHorariaDto.getIdEfector() != null &&
                        !Objects.equals(distribucionHoraria.getEfector().getId(),
                                distribucionHorariaDto.getIdEfector()))) {
            distribucionHoraria.setEfector(efectorService.findById(distribucionHorariaDto.getIdEfector()));
        } */

        if (distribucionHorariaDto.getFechaInicio() != distribucionHoraria.getFechaInicio()
                && distribucionHorariaDto.getFechaInicio() != null)
            distribucionHoraria.setFechaInicio(distribucionHorariaDto.getFechaInicio());

        if (distribucionHorariaDto.getFechaFinalizacion() != distribucionHoraria.getFechaFinalizacion()
                && distribucionHorariaDto.getFechaFinalizacion() != null)
            distribucionHoraria.setFechaFinalizacion(distribucionHorariaDto.getFechaFinalizacion());

        if (distribucionHorariaDto.getHoraIngreso() != distribucionHoraria.getHoraIngreso()
                && distribucionHorariaDto.getHoraIngreso() != null)
            distribucionHoraria.setHoraIngreso(distribucionHorariaDto.getHoraIngreso());

        return distribucionHoraria;
    }
}
