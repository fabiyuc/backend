package com.guardias.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Localidad;
import com.guardias.backend.service.LocalidadService;

@RestController
@RequestMapping("/localidad")
@CrossOrigin(origins = "http://localhost:4200")
public class LocalidadController {

    @Autowired
    LocalidadService localidadService;

    @GetMapping("/lista")
    public ResponseEntity<List<Localidad>> list() {
        List<Localidad> list = localidadService.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<List<Localidad>> getById(@PathVariable("id") Long id) {
        if (!localidadService.existsById(id))
            return new ResponseEntity(new Mensaje("localidad no existe"), HttpStatus.NOT_FOUND);
        Localidad localidad = localidadService.getById(id).get();
        return new ResponseEntity(localidad, HttpStatus.OK);
    }

    // TODO LocalidadController detalle por nombre
    // TODO LocalidadController create
    // TODO LocalidadController update
    // TODO LocalidadController delete
}
