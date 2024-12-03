package com.guardias.backend.controller;

import java.time.LocalDate;
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

import com.guardias.backend.dto.FeriadoDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Feriado;
import com.guardias.backend.service.FeriadoService;


@RestController
@RequestMapping("/feriado")
@CrossOrigin(origins = "http://localhost:4200")
public class FeriadoController {

    @Autowired
    FeriadoService feriadoService;

    @GetMapping("/list")
    public ResponseEntity<List<Feriado>> list() {
        List<Feriado> list = feriadoService.findByActivoTrue().get();
        return new ResponseEntity<List<Feriado>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Feriado>> listAll() {
        List<Feriado> list = feriadoService.findAll();
        return new ResponseEntity<List<Feriado>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Feriado> getById(@PathVariable("id") Long id) {
        if (!feriadoService.activo(id))
            return new ResponseEntity(new Mensaje("Fecha no encontrada"), HttpStatus.NOT_FOUND);
        Feriado feriado = feriadoService.findById(id).get();
        return new ResponseEntity(feriado, HttpStatus.OK);
    }

    @GetMapping("/detailmotivo/{motivo}")
    public ResponseEntity<List<Feriado>> getById(@PathVariable("motivo") String motivo) {
        if (!feriadoService.existsByMotivo(motivo))
            return new ResponseEntity(new Mensaje("Fecha no encontrada"), HttpStatus.NOT_FOUND);
        Feriado feriado = feriadoService.getByMotivo(motivo).get();
        return new ResponseEntity(feriado, HttpStatus.OK);
    }

    @GetMapping("/detail/{fecha}")
    public ResponseEntity<List<Feriado>> getByFecha(@PathVariable("fecha") LocalDate fecha) {
        if (!feriadoService.existsByFecha(fecha))
            return new ResponseEntity(new Mensaje("Fecha no encontrada"), HttpStatus.NOT_FOUND);
        Feriado feriado = feriadoService.getByFecha(fecha).get();
        return new ResponseEntity(feriado, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody FeriadoDto feriadoDto) {

        ResponseEntity<?> respuestaValidaciones = feriadoService.validations(feriadoDto, 0L);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Feriado feriado = feriadoService.createUpdate(new Feriado(), feriadoDto);
            feriadoService.save(feriado);
        }
        return respuestaValidaciones;
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody FeriadoDto feriadoDto) {
        if (!feriadoService.existsById(id))
            return new ResponseEntity(new Mensaje("El feriado no existe"), HttpStatus.BAD_REQUEST);

        ResponseEntity<?> respuestaValidaciones = feriadoService.validations(feriadoDto, id);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Feriado feriado = feriadoService.createUpdate(feriadoService.findById(id).get(), feriadoDto);
            feriadoService.save(feriado);
        }
        return respuestaValidaciones;
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!feriadoService.existsById(id))
            return new ResponseEntity(new Mensaje("el feriado no existe"), HttpStatus.NOT_FOUND);

        Feriado feriado = feriadoService.findById(id).get();
        feriado.setActivo(false);
        feriadoService.save(feriado);
        return new ResponseEntity(new Mensaje("feriado eliminado correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") Long id) {
        if (!feriadoService.existsById(id))
            return new ResponseEntity(new Mensaje("el feriado no existe"), HttpStatus.NOT_FOUND);
        feriadoService.deleteById(id);
        return new ResponseEntity(new Mensaje("feriado eliminado FISICAMENTE"), HttpStatus.OK);
    }

}
