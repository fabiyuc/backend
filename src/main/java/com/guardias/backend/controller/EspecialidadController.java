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
import com.guardias.backend.entity.Especialidad;
import com.guardias.backend.service.EspecialidadService;

@RestController
@RequestMapping("/especialidad")
@CrossOrigin(origins = "http://localhost:4200")
public class EspecialidadController {

    @Autowired
    EspecialidadService especialidadService;

    @GetMapping("/lista")
    public ResponseEntity<List<Especialidad>> list() {
        List<Especialidad> list = especialidadService.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<List<Especialidad>> getById(@PathVariable("id") Long id) {
        if (!especialidadService.existById(id))
            return new ResponseEntity(new Mensaje("especialidad no existe"), HttpStatus.NOT_FOUND);
        Especialidad especialidad = especialidadService.getById(id).get();
        return new ResponseEntity(especialidad, HttpStatus.OK);
    }

    // TODO (EspecialidadController create)
    // TODO (EspecialidadController update)
    // TODO (EspecialidadController delete)
    // TODO (EspecialidadController detalle por nombre)

}
