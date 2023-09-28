package com.guardias.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.ProfesionalDto;
import com.guardias.backend.dto.RegistroActividadDto;
import com.guardias.backend.modelo.Profesional;
import com.guardias.backend.modelo.RegistroActividad;
import com.guardias.backend.service.RegistroActividadServicio;

import io.micrometer.common.util.StringUtils;

@RestController
@RequestMapping("/registroActividad")
@CrossOrigin(origins = "http://localhost:4200")
public class RegistroActividadControlador {

    RegistroActividadServicio registroActividadServicio;

    @GetMapping("/lista")
    public ResponseEntity<List<RegistroActividad>> list() {
        List<RegistroActividad> list = registroActividadServicio.list();
        return new ResponseEntity<List<RegistroActividad>>(list, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody RegistroActividadDto registroActividadDto) {
        if (StringUtils.isBlank(registroActividadDto.getEstablecimiento()))
            return new ResponseEntity(new Mensaje("el establecimiento es obligatorio"),
                    HttpStatus.BAD_REQUEST);
        if (StringUtils.isBlank(registroActividadDto.getServicio()))
            return new ResponseEntity(new Mensaje("el servicio es obligatorio"),
                    HttpStatus.BAD_REQUEST);
        if (StringUtils.isBlank(registroActividadDto.getFechaIngreso().toString()))
            return new ResponseEntity(new Mensaje("la fecha de ingreso es obligatoria"),
                    HttpStatus.BAD_REQUEST);
        if (StringUtils.isBlank(registroActividadDto.getFechaEgreso().toString()))
            return new ResponseEntity(new Mensaje("la fecha de egreso es obligatoria"),
                    HttpStatus.BAD_REQUEST);
        if (StringUtils.isBlank(registroActividadDto.getHoraIngreso().toString()))
            return new ResponseEntity(new Mensaje("la hora de ingreso es obligatoria"),
                    HttpStatus.BAD_REQUEST);
        if (StringUtils.isBlank(registroActividadDto.getHoraEgreso().toString()))
            return new ResponseEntity(new Mensaje("la hora de egreso es obligatoria"),
                    HttpStatus.BAD_REQUEST);
        /*
         * if (registroActividadServicio.existsByNombre(profesionalDto.getNombre()))
         * return new ResponseEntity(new Mensaje("ese nombre ya existe"),
         * HttpStatus.BAD_REQUEST);
         */
        RegistroActividad registroActividad = new RegistroActividad(registroActividadDto.getEstablecimiento(),
                registroActividadDto.getServicio(), registroActividadDto.getFechaIngreso(),
                registroActividadDto.getFechaEgreso(), registroActividadDto.getHoraIngreso(),
                registroActividadDto.getHoraEgreso());
        registroActividadServicio.save(registroActividad);
        return new ResponseEntity(new Mensaje("Registro de Actividad creado"), HttpStatus.OK);
    }
}
