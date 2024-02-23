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

import com.guardias.backend.dto.AutoridadDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Autoridad;
import com.guardias.backend.service.AutoridadService;

@RestController
@RequestMapping("/autoridad")
@CrossOrigin(origins = "http://localhost:4200")
public class AutoridadController {

    @Autowired
    AutoridadService autoridadService;

    @GetMapping("/list")
    public ResponseEntity<List<Autoridad>> list() {
        List<Autoridad> list = autoridadService.list();
        return new ResponseEntity<List<Autoridad>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Autoridad> getById(@PathVariable("id") Long id) {
        if (!autoridadService.existsById(id))
            return new ResponseEntity(new Mensaje("No existe la autoridad"), HttpStatus.NOT_FOUND);
        Autoridad autoridad = autoridadService.findById(id).get();
        return new ResponseEntity<Autoridad>(autoridad, HttpStatus.OK);
    }

    // TODO hacer las validaciones y el createUpdate
    private ResponseEntity<?> validations(AutoridadDto autoridadDto) {
        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody AutoridadDto autoridadDto) {

        Autoridad autoridad = new Autoridad();
        autoridad.setNombre(autoridadDto.getNombre());
        autoridad.setFechaInicio(autoridadDto.getFechaInicio());
        autoridad.setFechaFinal(autoridadDto.getFechaFinal());
        autoridad.setEsActual(autoridadDto.isEsActual());
        autoridad.setEsRegional(autoridadDto.isEsRegional());
        autoridadService.save(autoridad);
        return new ResponseEntity(new Mensaje("Autoridad creada"), HttpStatus.OK);
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody AutoridadDto autoridadDto) {
        if (!autoridadService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la autoridad"), HttpStatus.NOT_FOUND);

        Autoridad autoridad = autoridadService.findById(id).get();

        if (!autoridadDto.getNombre().equals(autoridad.getNombre()))
            autoridad.setNombre(autoridadDto.getNombre());

        if (autoridad.getFechaInicio() != autoridadDto.getFechaInicio() && autoridadDto.getFechaInicio() != null)
            autoridad.setFechaInicio(autoridadDto.getFechaInicio());

        if (autoridad.getFechaFinal() != autoridadDto.getFechaFinal() && autoridadDto.getFechaFinal() != null)
            autoridad.setFechaFinal(autoridadDto.getFechaFinal());

        if (autoridad.getFechaFinal() != autoridadDto.getFechaFinal() && autoridadDto.getFechaFinal() != null)
            autoridad.setFechaFinal(autoridadDto.getFechaFinal());

        autoridad.setEsActual(autoridadDto.isEsActual());
        autoridad.setEsRegional(autoridadDto.isEsRegional());
        autoridadService.save(autoridad);
        return new ResponseEntity(new Mensaje("La autoridad ha sido actualizada"), HttpStatus.OK);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!autoridadService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la autoridad"), HttpStatus.NOT_FOUND);
        Autoridad autoridad = autoridadService.findById(id).get();
        autoridad.setActivo(false);
        autoridadService.save(autoridad);
        return new ResponseEntity(new Mensaje("autoridad eliminada"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {

        if (!autoridadService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la autoridad"), HttpStatus.NOT_FOUND);
        autoridadService.deleteById(id);
        return new ResponseEntity(new Mensaje("autoridad eliminada FISICAMENTE"), HttpStatus.OK);
    }
}
