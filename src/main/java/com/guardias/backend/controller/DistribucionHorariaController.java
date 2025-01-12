package com.guardias.backend.controller;

import java.util.List;
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
import com.guardias.backend.entity.Person;
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

        // Verifica si la persona tiene legajos y si el Efector del legajo coincide con
        // el Efector del DTO
        if (distribucionHoraria.getPersona() != null) {
            Person persona = distribucionHoraria.getPersona();
            List<Legajo> legajosPersona = persona.getLegajos();

            // Validar que la persona tenga legajos
            if (legajosPersona == null || legajosPersona.isEmpty()) {
                throw new IllegalArgumentException("La persona no tiene legajos registrados.");
            }

            // Buscar el primer legajo válido (que no sea autoridad)
            Legajo legajoValido = legajosPersona.stream()
                    .filter(legajo -> Boolean.FALSE.equals(legajo.getEsAutoridad()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "La persona no tiene un legajo válido (no es autoridad)."));

            // Validar que el legajo tenga un Efector asociado
            List<Efector> efectoresPersona = legajoValido.getEfectores();
            if (efectoresPersona == null || efectoresPersona.isEmpty()) {
                throw new IllegalArgumentException("El legajo válido de la persona no tiene efectores asociados.");
            }

            // Validar que el Efector especificado en el DTO existe
            Efector efectorDto = efectorService.findById(distribucionHorariaDto.getIdEfector());
            if (efectorDto == null) {
                throw new IllegalArgumentException("El Efector especificado en el DTO no existe.");
            }

            // Validar si el Efector del DTO está en la lista de efectores del legajo válido
            boolean efectorCoincide = efectoresPersona.stream()
                    .anyMatch(efector -> Objects.equals(efector.getId(), efectorDto.getId()));

            if (!efectorCoincide) {
                throw new IllegalArgumentException(
                        "El Efector del DTO no coincide con los efectores asociados al legajo válido de la persona.");
            }
        } else {
            throw new IllegalArgumentException("No se ha encontrado la persona asociada a la Distribución Horaria.");
        }

        if (distribucionHoraria.getEfector() == null ||
                (distribucionHorariaDto.getIdEfector() != null &&
                        !Objects.equals(distribucionHoraria.getEfector().getId(),
                                distribucionHorariaDto.getIdEfector()))) {
            distribucionHoraria.setEfector(efectorService.findById(distribucionHorariaDto
                    .getIdEfector()));
        }

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
