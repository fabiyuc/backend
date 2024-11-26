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
import com.guardias.backend.dto.PermisosDto;
import com.guardias.backend.entity.Permisos;
import com.guardias.backend.service.PermisosService;

@RestController
@RequestMapping("/permisos")
@CrossOrigin(origins = "http://localhost:4200")
public class PermisosController {

    @Autowired
    PermisosService permisosService;

    // la lista de efectores debería actualizarse si hay bajas de efectores?
    @GetMapping("/list")
    public ResponseEntity<List<Permisos>> list() {
        List<Permisos> permisosList = permisosService.findByActivoTrue().orElse(new ArrayList<>());

        return new ResponseEntity<List<Permisos>>(permisosList, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Permisos>> listAll() {
        List<Permisos> list = permisosService.findAll();
        return new ResponseEntity<List<Permisos>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAsistencialesByEfector/{idEfector}")
    public ResponseEntity<List<Permisos>> listPermisosByEfectorAndAsistencial(@PathVariable Long idEfector) {
        List<Permisos> permisos = permisosService.getPermisosByEfectorAndAsistencial(idEfector);
        return new ResponseEntity<>(permisos, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Permisos> getById(@PathVariable("id") Long id) {
        if (!permisosService.activo(id))
            return new ResponseEntity(new Mensaje("No existe el permiso"), HttpStatus.NOT_FOUND);
        Permisos permisos = permisosService.findById(id).get();
        return new ResponseEntity<Permisos>(permisos, HttpStatus.OK);
    }

    @GetMapping("/detailAsistencial/{idPersona}")
    public ResponseEntity<Permisos> getByAsistencial(@PathVariable("idPersona") Long idPersona) {
        if (!permisosService.activoByPersona(idPersona))
            return new ResponseEntity(new Mensaje("no existe el permiso de este asistencial"), HttpStatus.NOT_FOUND);
        Permisos permisos = permisosService.findByPersona(idPersona).get();
        return new ResponseEntity<Permisos>(permisos, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody PermisosDto permisosDto) {

        ResponseEntity<?> respuestaValidaciones = permisosService.validations(permisosDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            Permisos permisos = permisosService.createUpdate(new Permisos(), permisosDto);
            permisosService.save(permisos);
            return new ResponseEntity(new Mensaje("permiso creado"), HttpStatus.OK);
        }
        return respuestaValidaciones;
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody PermisosDto permisosDto) {
        if (!permisosService.activo(id))
            return new ResponseEntity(new Mensaje("no existe el permiso"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = permisosService.validations(permisosDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            Permisos permisos = permisosService.createUpdate(permisosService.findById(id).get(), permisosDto);
            permisosService.save(permisos);
            return new ResponseEntity(new Mensaje("permiso modificado"), HttpStatus.OK);
        }
        return respuestaValidaciones;
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!permisosService.activo(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        Permisos permisos = permisosService.findById(id).get();
        permisos.setActivo(false);
        permisosService.save(permisos);
        return new ResponseEntity<>(new Mensaje("Permiso eliminado correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!permisosService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        permisosService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("Permiso eliminado FISICAMENTE"), HttpStatus.OK);
    }

    @GetMapping("/tienePermisos/{idPersona}/{idEfector}")
    public boolean tienePermisos( @PathVariable("idPersona") long idPersona, @PathVariable("idEfector") long idEfector) {
        return permisosService.tienePermisos(idPersona, idEfector);
    }

}
