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

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.ValorGmiDto;
import com.guardias.backend.entity.ValorGmi;
import com.guardias.backend.service.ValorGmiService;

@RestController
@RequestMapping("/valorGmi")
@CrossOrigin(origins = "http://localhost:4200")
public class ValorGmiController {

    @Autowired
    ValorGmiService valorGmiService;

    @GetMapping("/list")
    public ResponseEntity<ValorGmi> valorGmiActivo() {
        ValorGmi activo = valorGmiService.findByActivoTrue();
        return new ResponseEntity<ValorGmi>(activo, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<ValorGmi>> listAll() {
        List<ValorGmi> list = valorGmiService.findAll();
        return new ResponseEntity<List<ValorGmi>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ValorGmi> getById(@PathVariable("id") Long id) {

        if (!valorGmiService.activo(id))
            return new ResponseEntity(new Mensaje("Valor no encontrado"), HttpStatus.NOT_FOUND);
        ValorGmi valorGmi = valorGmiService.findById(id).get();
        return new ResponseEntity(valorGmi, HttpStatus.OK);
    }

    // @GetMapping("/findByDate/{fecha}")
    // public ResponseEntity<List<ValorGmi>> findByDate(LocalDate fecha) {
    // List<ValorGmi> list = valorGmiService.findByDate(fecha);

    // if (list == null)
    // return new ResponseEntity(new Mensaje("Valor no encontrado"),
    // HttpStatus.NOT_FOUND);

    // return new ResponseEntity<List<ValorGmi>>(list, HttpStatus.OK);
    // }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ValorGmiDto valorGmiDto) {
        ResponseEntity<?> respuestaValidaciones = valorGmiService.validations(valorGmiDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            ValorGmi valorGmi = valorGmiService.createUpdate(new ValorGmi(), valorGmiDto);
            valorGmiService.save(valorGmi);
            return new ResponseEntity(new Mensaje("Valor creado correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody ValorGmiDto valorGmiDto) {

        if (!valorGmiService.activo(id))
            return new ResponseEntity(new Mensaje("El valor no existe"), HttpStatus.NOT_FOUND);
        ResponseEntity<?> respuestaValidaciones = valorGmiService.validations(valorGmiDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            ValorGmi valorGmi = valorGmiService.createUpdate(valorGmiService.findById(id).get(), valorGmiDto);
            valorGmiService.save(valorGmi);
            return new ResponseEntity(new Mensaje("Valor actualizado correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        return valorGmiService.logicDelete(id);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!valorGmiService.existsById(id))
            return new ResponseEntity(new Mensaje("El valor no existe"), HttpStatus.NOT_FOUND);

        valorGmiService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("Valor eliminado FISICAMENTEE"), HttpStatus.OK);
    }
}
