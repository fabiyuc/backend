package com.guardias.backend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.PersonDto;
import com.guardias.backend.entity.DistribucionHoraria;
import com.guardias.backend.entity.Legajo;
import com.guardias.backend.entity.NovedadPersonal;
import com.guardias.backend.entity.Person;
import com.guardias.backend.service.DistribucionHorariaService;
import com.guardias.backend.service.LegajoService;
import com.guardias.backend.service.NovedadPersonalService;
import com.guardias.backend.service.PersonService;

import io.micrometer.common.util.StringUtils;

@RestController
public class PersonController {

    @Autowired
    PersonService personService;
    @Autowired
    NovedadPersonalService novedadPersonalService;
    @Autowired
    LegajoService legajoService;
    @Autowired
    DistribucionHorariaService distribucionHorariaService;

    public ResponseEntity<?> validationsCreate(PersonDto personDto) {
        ResponseEntity<?> respuestaValidaciones = validations(personDto);

        if (personService.existsByDni(personDto.getDni()))
            return new ResponseEntity<>(new Mensaje("El DNI ya existe"), HttpStatus.BAD_REQUEST);

        if (personService.existsByCui(personDto.getCuil()))
            return new ResponseEntity<>(new Mensaje("El CUIL ya existe"), HttpStatus.BAD_REQUEST);

        return respuestaValidaciones;
    }

    public ResponseEntity<?> validations(PersonDto personDto) {
        if (StringUtils.isBlank(personDto.getNombre())) {
            return new ResponseEntity<>(new Mensaje("El Nombre es obligatorio"), HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(personDto.getApellido())) {
            return new ResponseEntity<>(new Mensaje("El Apellido es obligatorio"), HttpStatus.BAD_REQUEST);
        }
        if (personDto.getDni() < 1000000)
            return new ResponseEntity<>(new Mensaje("DNI es incorrecto"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(personDto.getCuil())) {
            return new ResponseEntity<>(new Mensaje("El Cuil es obligatorio"), HttpStatus.BAD_REQUEST);
        }
        if (personDto.getFechaNacimiento() == null) {
            return new ResponseEntity(new Mensaje("La fecha de nacimiento es obligatoria"), HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(personDto.getSexo())) {
            return new ResponseEntity<>(new Mensaje("Es obligatorio indicar el sexo"), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    public Person createUpdate(Person person, PersonDto personDto) {

        if (personDto.getTelefono() != null && !personDto.getTelefono().equals(person.getTelefono())
                && !personDto.getTelefono().isEmpty())
            person.setTelefono(personDto.getTelefono());

        if (personDto.getIdSuplentes() != null) {
            List<Long> idList = new ArrayList();
            if (person.getSuplentes() != null) {
                for (NovedadPersonal novedadPersonal : person.getSuplentes()) {
                    for (Long id : personDto.getIdSuplentes()) {
                        if (!novedadPersonal.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            }

            List<Long> idsToAdd = idList.isEmpty() ? personDto.getIdSuplentes() : idList;
            for (Long id : idsToAdd) {
                person.getSuplentes().add(novedadPersonalService.findById(id).get());
                novedadPersonalService.findById(id).get().setSuplente(person);
            }
        }

        if (personDto.getIdNovedadesPersonales() != null) {
            List<Long> idList = new ArrayList();
            if (person.getNovedadesPersonales() != null) {
                for (NovedadPersonal novedadPersonal : person.getNovedadesPersonales()) {
                    for (Long id : personDto.getIdNovedadesPersonales()) {
                        if (!novedadPersonal.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            }

            List<Long> idsToAdd = idList.isEmpty() ? personDto.getIdNovedadesPersonales() : idList;
            for (Long id : idsToAdd) {
                person.getNovedadesPersonales().add(novedadPersonalService.findById(id).get());
                novedadPersonalService.findById(id).get().setPersona(person);
            }
        }

        if (personDto.getSexo() != null && !personDto.getSexo().equals(person.getSexo())
                && !personDto.getSexo().isEmpty())
            person.setSexo(personDto.getSexo());

        if (personDto.getNombre() != null && !personDto.getNombre().equals(person.getNombre())
                && !personDto.getNombre().isEmpty())
            person.setNombre(personDto.getNombre());

        if (personDto.getIdLegajos() != null) {
            List<Long> idList = new ArrayList();
            if (person.getLegajos() != null) {
                for (Legajo legajo : person.getLegajos()) {
                    for (Long id : personDto.getIdLegajos()) {
                        if (!legajo.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            }

            List<Long> idsToAdd = idList.isEmpty() ? personDto.getIdLegajos() : idList;
            for (Long id : idsToAdd) {
                person.getLegajos().add(legajoService.findById(id).get());
                legajoService.findById(id).get().setPersona(person);
            }
        }

        if (!personDto.getFechaNacimiento().equals(person.getFechaNacimiento()))
            if (personDto.getFechaNacimiento() != null && personDto.getFechaNacimiento() != person.getFechaNacimiento())
                person.setFechaNacimiento(personDto.getFechaNacimiento());

        person.setEsAsistencial(personDto.getEsAsistencial());

        if (personDto.getEmail() != null && !personDto.getEmail().equals(person.getEmail())
                && !personDto.getEmail().isEmpty())
            person.setEmail(personDto.getEmail());

        if (personDto.getDomicilio() != null && !personDto.getDomicilio().equals(person.getDomicilio())
                && !personDto.getDomicilio().isEmpty())
            person.setDomicilio(personDto.getDomicilio());

        if (personDto.getDni() != person.getDni())
            person.setDni(personDto.getDni());

        if (personDto.getIdDistribucionesHorarias() != null) {
            List<Long> idList = new ArrayList();
            if (person.getDistribucionesHorarias() != null) {
                for (DistribucionHoraria distribucionHoraria : person.getDistribucionesHorarias()) {
                    for (Long id : personDto.getIdDistribucionesHorarias()) {
                        if (!distribucionHoraria.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            }

            List<Long> idsToAdd = idList.isEmpty() ? personDto.getIdDistribucionesHorarias() : idList;
            for (Long id : idsToAdd) {
                person.getDistribucionesHorarias().add(distribucionHorariaService.findById(id));
                distribucionHorariaService.findById(id).setPersona(person);
            }
        }

        if (personDto.getCuil() != null && !personDto.getCuil().equals(person.getCuil())
                && !personDto.getCuil().isEmpty())
            person.setCuil(personDto.getCuil());

        if (personDto.getApellido() != null && !personDto.getApellido().equals(person.getApellido())
                && !personDto.getApellido().isEmpty())
            person.setApellido(personDto.getApellido());
        return person;
    }
}
