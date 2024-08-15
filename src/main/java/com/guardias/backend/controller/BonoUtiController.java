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

import com.guardias.backend.dto.BonoUtiDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.ValorGmiDto;
import com.guardias.backend.entity.BonoUti;
import com.guardias.backend.service.BonoUtiService;

@RestController
@RequestMapping("/bonoUti")
@CrossOrigin(origins = "http://localhost:4200")
public class BonoUtiController {

    @Autowired
    BonoUtiService bonoUtiService;
    
    @GetMapping("/list")
    public ResponseEntity<List<BonoUti>> list() {
        List<BonoUti> list = bonoUtiService.findByActivoTrue().get();
        return new ResponseEntity<List<BonoUti>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<BonoUti>> listAll() {
        List<BonoUti> list = bonoUtiService.findAll();
        return new ResponseEntity<List<BonoUti>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<BonoUti> getById(@PathVariable("id") Long id) {

        if (!bonoUtiService.activo(id))
            return new ResponseEntity(new Mensaje("Valor no encontrado"), HttpStatus.NOT_FOUND);
            BonoUti bonoUti = bonoUtiService.findById(id).get();
        return new ResponseEntity(bonoUti, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody BonoUtiDto bonoUtiDto) {
        ResponseEntity<?> respuestaValidaciones = bonoUtiService.validations(bonoUtiDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            BonoUti bonoUti = bonoUtiService.createUpdate(new BonoUti(), bonoUtiDto);
            bonoUtiService.save(bonoUti);
            return new ResponseEntity(new Mensaje("Valor creado correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody BonoUtiDto bonoUtiDto) {

        if (!bonoUtiService.activo(id))
            return new ResponseEntity(new Mensaje("El valor no existe"), HttpStatus.NOT_FOUND);
        ResponseEntity<?> respuestaValidaciones = bonoUtiService.validations(bonoUtiDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            BonoUti bonoUti = bonoUtiService.createUpdate(bonoUtiService.findById(id).get(), bonoUtiDto);
            bonoUtiService.save(bonoUti);
            return new ResponseEntity(new Mensaje("Valor actualizado correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!bonoUtiService.activo(id))
            return new ResponseEntity(new Mensaje("El valor no existe"), HttpStatus.NOT_FOUND);

        return bonoUtiService.logicDelete(id);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!bonoUtiService.existsById(id))
            return new ResponseEntity(new Mensaje("El valor no existe"), HttpStatus.NOT_FOUND);

        bonoUtiService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("Valor eliminado FISICAMENTEE"), HttpStatus.OK);
    }
}
