package com.guardias.backend.controller;

import java.util.ArrayList;
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
import com.guardias.backend.dto.HabilitacionesGeneralesDto;
import com.guardias.backend.entity.HabilitacionesGenerales;
import com.guardias.backend.service.HabilitacionesGeneralesService;

@RestController
@RequestMapping("/habilitacionesGenerales")
@CrossOrigin(origins = "http://localhost:4200")
public class HabilitacionesGeneralesController {

    @Autowired
    HabilitacionesGeneralesService habilitacionesGeneralesService;

    // la lista de efectores debería actualizarse si hay bajas de efectores?
    @GetMapping("/list")
    public ResponseEntity<List<HabilitacionesGenerales>> list() {
        List<HabilitacionesGenerales> habilitacionesGeneralesList = habilitacionesGeneralesService.findByActivoTrue()
                .orElse(new ArrayList<>());

        return new ResponseEntity<List<HabilitacionesGenerales>>(habilitacionesGeneralesList, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<HabilitacionesGenerales>> listAll() {
        List<HabilitacionesGenerales> list = habilitacionesGeneralesService.findAll();
        return new ResponseEntity<List<HabilitacionesGenerales>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAsistencialesByEfector/{idEfector}")
    public ResponseEntity<List<HabilitacionesGenerales>> listHabilitacionesGeneralesByEfectorAndAsistencial(
            @PathVariable Long idEfector) {
        List<HabilitacionesGenerales> habilitacionesGenerales = habilitacionesGeneralesService
                .getHabilitacionesGeneralesByEfectorAndAsistencial(idEfector);
        return new ResponseEntity<>(habilitacionesGenerales, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<HabilitacionesGenerales> getById(@PathVariable("id") Long id) {
        if (!habilitacionesGeneralesService.activo(id))
            return new ResponseEntity(new Mensaje("No existe la habilitacion general con ese id"),
                    HttpStatus.NOT_FOUND);
                    HabilitacionesGenerales habilitacionesGeneral = habilitacionesGeneralesService.findById(id).get();
        return new ResponseEntity<HabilitacionesGenerales>(habilitacionesGeneral, HttpStatus.OK);
    }

    @GetMapping("/detailAsistencial/{idPersona}")
    public ResponseEntity<HabilitacionesGenerales> getByAsistencial(@PathVariable("idPersona") Long idPersona) {
        if (!habilitacionesGeneralesService.activoByPersona(idPersona))
            return new ResponseEntity(new Mensaje("no existe la habilitacion general de este asistencial"),
                    HttpStatus.NOT_FOUND);
                    HabilitacionesGenerales habilitacionesGenerales = habilitacionesGeneralesService.findByPersona(idPersona).get();
        return new ResponseEntity<HabilitacionesGenerales>(habilitacionesGenerales, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody HabilitacionesGeneralesDto habilitacionesGeneralesDto) {

        ResponseEntity<?> respuestaValidaciones = habilitacionesGeneralesService.validations(habilitacionesGeneralesDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            HabilitacionesGenerales habilitacionesGenerales = habilitacionesGeneralesService
                    .createUpdate(new HabilitacionesGenerales(), habilitacionesGeneralesDto);
            habilitacionesGeneralesService.save(habilitacionesGenerales);
            return new ResponseEntity(new Mensaje("habilitaciones generales creado"), HttpStatus.OK);
        }
        return respuestaValidaciones;
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id,
            @RequestBody HabilitacionesGeneralesDto habilitacionesGeneralesDto) {
        if (!habilitacionesGeneralesService.activo(id))
            return new ResponseEntity(new Mensaje("no existe la habilitaciones generales"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = habilitacionesGeneralesService.validations(habilitacionesGeneralesDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            HabilitacionesGenerales habilitacionesGenerales = habilitacionesGeneralesService
                    .createUpdate(habilitacionesGeneralesService.findById(id).get(), habilitacionesGeneralesDto);
            habilitacionesGeneralesService.save(habilitacionesGenerales);
            return new ResponseEntity(new Mensaje("habilitaciones generales modificado"), HttpStatus.OK);
        }
        return respuestaValidaciones;
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!habilitacionesGeneralesService.activo(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

            HabilitacionesGenerales habilitacionesGenerales = habilitacionesGeneralesService.findById(id).get();
        habilitacionesGenerales.setActivo(false);
        habilitacionesGeneralesService.save(habilitacionesGenerales);
        return new ResponseEntity<>(new Mensaje("habilitaciones generales eliminado correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!habilitacionesGeneralesService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        habilitacionesGeneralesService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("habilitaciones generales eliminada FISICAMENTE"), HttpStatus.OK);
    }

    @GetMapping("/tieneHabilitacionesGenerales/{idPersona}/{idEfector}")
    public boolean tieneHabilitacionesGenerales(@PathVariable("idPersona") long idPersona,
            @PathVariable("idEfector") long idEfector) {
        return habilitacionesGeneralesService.tieneHabilitacionesGenerales(idPersona, idEfector);
    }
}
