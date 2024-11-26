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
import com.guardias.backend.dto.HabilitacionesGuardiasDto;
import com.guardias.backend.entity.HabilitacionesGuardias;
import com.guardias.backend.service.HabilitacionesGuardiasService;

@RestController
@RequestMapping("/habilitacionesGuardias")
@CrossOrigin(origins = "http://localhost:4200")
public class HabilitacionesGuardiasController {

    @Autowired
    HabilitacionesGuardiasService habilitacionesGuardiasService;

    // la lista de efectores debería actualizarse si hay bajas de efectores?
    @GetMapping("/list")
    public ResponseEntity<List<HabilitacionesGuardias>> list() {
        List<HabilitacionesGuardias> habilitacionesGuardiasList = habilitacionesGuardiasService.findByActivoTrue().orElse(new ArrayList<>());

        return new ResponseEntity<List<HabilitacionesGuardias>>(habilitacionesGuardiasList, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<HabilitacionesGuardias>> listAll() {
        List<HabilitacionesGuardias> list = habilitacionesGuardiasService.findAll();
        return new ResponseEntity<List<HabilitacionesGuardias>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAsistencialesByEfector/{idEfector}")
    public ResponseEntity<List<HabilitacionesGuardias>> listHabilitacionesGuardiasByEfectorAndAsistencial(@PathVariable Long idEfector) {
        List<HabilitacionesGuardias> habilitacionesGuardias = habilitacionesGuardiasService.getHabilitacionesGuardiasByEfectorAndAsistencial(idEfector);
        return new ResponseEntity<>(habilitacionesGuardias, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<HabilitacionesGuardias> getById(@PathVariable("id") Long id) {
        if (!habilitacionesGuardiasService.activo(id))
            return new ResponseEntity(new Mensaje("No existe la habilitacion de guardia con ese id"), HttpStatus.NOT_FOUND);
        HabilitacionesGuardias habilitacionesGuardias = habilitacionesGuardiasService.findById(id).get();
        return new ResponseEntity<HabilitacionesGuardias>(habilitacionesGuardias, HttpStatus.OK);
    }

    @GetMapping("/detailAsistencial/{idPersona}")
    public ResponseEntity<HabilitacionesGuardias> getByAsistencial(@PathVariable("idPersona") Long idPersona) {
        if (!habilitacionesGuardiasService.activoByPersona(idPersona))
            return new ResponseEntity(new Mensaje("no existe la habilitacion de guardia de este asistencial"), HttpStatus.NOT_FOUND);
        HabilitacionesGuardias habilitacionesGuardias = habilitacionesGuardiasService.findByPersona(idPersona).get();
        return new ResponseEntity<HabilitacionesGuardias>(habilitacionesGuardias, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody HabilitacionesGuardiasDto habilitacionesGuardiasDto) {

        ResponseEntity<?> respuestaValidaciones = habilitacionesGuardiasService.validations(habilitacionesGuardiasDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            HabilitacionesGuardias habilitacionesGuardias = habilitacionesGuardiasService.createUpdate(new HabilitacionesGuardias(), habilitacionesGuardiasDto);
            habilitacionesGuardiasService.save(habilitacionesGuardias);
            return new ResponseEntity(new Mensaje("habilitaciones de Guardias creado"), HttpStatus.OK);
        }
        return respuestaValidaciones;
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody HabilitacionesGuardiasDto habilitacionesGuardiasDto) {
        if (!habilitacionesGuardiasService.activo(id))
            return new ResponseEntity(new Mensaje("no existe la habilitaciones de Guardias"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = habilitacionesGuardiasService.validations(habilitacionesGuardiasDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            HabilitacionesGuardias habilitacionesGuardias = habilitacionesGuardiasService.createUpdate(habilitacionesGuardiasService.findById(id).get(), habilitacionesGuardiasDto);
            habilitacionesGuardiasService.save(habilitacionesGuardias);
            return new ResponseEntity(new Mensaje("habilitaciones de Guardias modificado"), HttpStatus.OK);
        }
        return respuestaValidaciones;
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!habilitacionesGuardiasService.activo(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        HabilitacionesGuardias habilitacionesGuardias = habilitacionesGuardiasService.findById(id).get();
        habilitacionesGuardias.setActivo(false);
        habilitacionesGuardiasService.save(habilitacionesGuardias);
        return new ResponseEntity<>(new Mensaje("habilitaciones de Guardias eliminado correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!habilitacionesGuardiasService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        habilitacionesGuardiasService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("habilitaciones de Guardias eliminada FISICAMENTE"), HttpStatus.OK);
    }

    @GetMapping("/tieneHabilitacionesGuardias/{idPersona}/{idEfector}")
    public boolean tieneHabilitacionesGuardias( @PathVariable("idPersona") long idPersona, @PathVariable("idEfector") long idEfector) {
        return habilitacionesGuardiasService.tieneHabilitacionesGuardias(idPersona, idEfector);
    }

}
