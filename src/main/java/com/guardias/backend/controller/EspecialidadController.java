package com.guardias.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guardias.backend.dto.EspecialidadDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Especialidad;
import com.guardias.backend.service.EspecialidadService;

import io.micrometer.common.util.StringUtils;

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
        if (!especialidadService.existsById(id))
            return new ResponseEntity(new Mensaje("especialidad no existe"), HttpStatus.NOT_FOUND);
        Especialidad especialidad = especialidadService.getById(id).get();
        return new ResponseEntity(especialidad, HttpStatus.OK);
    }

    @GetMapping("/detallenombre/{nombre}")
    public ResponseEntity<List<Especialidad>> getByNombre(@PathVariable("nombre") String nombre) {
        if (!especialidadService.existsByNombre(nombre))
            return new ResponseEntity(new Mensaje("especialidad no existe"), HttpStatus.NOT_FOUND);
        Especialidad especialidad = especialidadService.getEspecialidadByNombre(nombre).get();
        return new ResponseEntity(especialidad, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody EspecialidadDto especialidadDto) {
        if (StringUtils.isBlank(especialidadDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        Especialidad especialidad = new Especialidad();
        especialidad.setNombre(especialidadDto.getNombre());
        especialidad.setProfesion(especialidadDto.getProfesion());

        especialidadService.save(especialidad);
        return new ResponseEntity(new Mensaje("Especialidad creada"), HttpStatus.OK);
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody EspecialidadDto especialidadDto) {
        if (StringUtils.isBlank(especialidadDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        Especialidad especialidad = especialidadService.getById(id).get();
        especialidad.setNombre(especialidadDto.getNombre());
        especialidadService.save(especialidad);
        return new ResponseEntity(new Mensaje("Especialidad modificada"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {

        if (!especialidadService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el especialidad"), HttpStatus.NOT_FOUND);
        especialidadService.deleteById(id);
        return new ResponseEntity(new Mensaje("especialidad eliminado"), HttpStatus.OK);
    }

}
