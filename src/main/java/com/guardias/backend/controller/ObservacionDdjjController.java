package com.guardias.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.ObservacionDdjjDto;
import com.guardias.backend.entity.ObservacionDdjj;
import com.guardias.backend.service.ObservacionDdjjService;

@Controller
@RequestMapping("/observacionDdjj")
public class ObservacionDdjjController {
    
    @Autowired
    ObservacionDdjjService observacionDdjjService;

    @GetMapping("/list")
    public ResponseEntity<List<ObservacionDdjj>> list() {
        List<ObservacionDdjj> list = observacionDdjjService.findByActivoTrue();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<ObservacionDdjj>> listAll() {
        List<ObservacionDdjj> list = observacionDdjjService.findAll();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<ObservacionDdjj>> getById(@PathVariable("id") Long id) {
        if (!observacionDdjjService.activo(id))
            return new ResponseEntity(new Mensaje("No existe la observacion de ddjj"),
                    HttpStatus.NOT_FOUND);
        ObservacionDdjj observacionDdjj = observacionDdjjService.findById(id).get();
        return new ResponseEntity(observacionDdjj, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ObservacionDdjjDto observacionDdjjDto) {
        ResponseEntity<?> respuestaValidaciones = observacionDdjjService.validations(observacionDdjjDto, 0L);
        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            ObservacionDdjj observacionDdjj = observacionDdjjService.createUpdate(new ObservacionDdjj(), observacionDdjjDto);
            observacionDdjjService.save(observacionDdjj);

            return new ResponseEntity(new Mensaje("Observacion de ddjj creada"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody ObservacionDdjjDto observacionDdjjDto) {
        if (!observacionDdjjService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la observacion de ddjj"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = observacionDdjjService.validations(observacionDdjjDto, id);
        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            ObservacionDdjj observacionDdjj = observacionDdjjService.createUpdate(observacionDdjjService.findById(id).get(), observacionDdjjDto);
            observacionDdjjService.save(observacionDdjj);

            return new ResponseEntity(new Mensaje("Observacion de ddjj modificada"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!observacionDdjjService.activo(id))
            return new ResponseEntity(new Mensaje("observacion de ddjj no encontrada"), HttpStatus.NOT_FOUND);

        ObservacionDdjj observacionDdjj = observacionDdjjService.findById(id).get();
        observacionDdjj.setActivo(false);
        observacionDdjjService.save(observacionDdjj);
        return new ResponseEntity(new Mensaje("observacion eliminada LOGICAMENTE"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") Long id) {
        if (!observacionDdjjService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la observacion de ddjj"), HttpStatus.NOT_FOUND);
        observacionDdjjService.deleteById(id);
        return new ResponseEntity(new Mensaje("Observacion eliminada FISICAMENTE"), HttpStatus.OK);
    }


}
