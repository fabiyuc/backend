package com.guardias.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.PersonDto;
import com.guardias.backend.entity.Person;
import io.micrometer.common.util.StringUtils;

@RestController
public class PersonController {

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

        if (personDto.getEstado() == null)
            return new ResponseEntity(new Mensaje("indicar si el estado es True o False"), HttpStatus.BAD_REQUEST);
        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    public Person createUpdate(Person person, PersonDto personDto) {
        if (!personDto.getTelefono().equals(person.getTelefono()))
            person.setTelefono(personDto.getTelefono());
        if (!personDto.getSuplentes().equals(person.getSuplentes()))
            person.setSuplentes(personDto.getSuplentes());
        if (!personDto.getSexo().equals(person.getSexo()))
            person.setSexo(personDto.getSexo());
        if (!personDto.getNovedadesPersonales().equals(person.getNovedadesPersonales()))
            person.setNovedadesPersonales(personDto.getNovedadesPersonales());
        if (!personDto.getNombre().equals(person.getNombre()))
            person.setNombre(personDto.getNombre());
        if (!personDto.getLegajos().equals(person.getLegajos()))
            person.setLegajos(personDto.getLegajos());
        if (!personDto.getFechaNacimiento().equals(person.getFechaNacimiento()))
            person.setFechaNacimiento(personDto.getFechaNacimiento());
        if (!personDto.getEstado().equals(person.getEstado()))
            person.setEstado(personDto.getEstado());
        if (!personDto.getEmail().equals(person.getEmail()))
            person.setEmail(personDto.getEmail());
        if (!personDto.getDomicilio().equals(person.getDomicilio()))
            person.setDomicilio(personDto.getDomicilio());
        if (personDto.getDni() != person.getDni())
            person.setDni(personDto.getDni());
        if (!personDto.getDistribucionesHorarias().equals(person.getDistribucionesHorarias()))
            person.setDistribucionesHorarias(personDto.getDistribucionesHorarias());
        if (!personDto.getCuil().equals(person.getCuil()))
            person.setCuil(personDto.getCuil());
        if (!personDto.getApellido().equals(person.getApellido()))
            person.setApellido(personDto.getApellido());
        return person;
    }

}
