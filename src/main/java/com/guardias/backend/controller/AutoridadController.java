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
import com.guardias.backend.service.CargoService;
import com.guardias.backend.service.EfectorService;
import com.guardias.backend.service.PersonService;

@RestController
@RequestMapping("/autoridad")
@CrossOrigin(origins = "http://localhost:4200")
public class AutoridadController {

    @Autowired
    AutoridadService autoridadService;
    @Autowired
    EfectorService efectorService;
    @Autowired
    PersonService personService;
    @Autowired
    CargoService cargoService;

    @GetMapping("/list")
    public ResponseEntity<List<Autoridad>> list() {
        List<Autoridad> list = autoridadService.findByActivoTrue().get();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Autoridad>> listAll() {
        List<Autoridad> list = autoridadService.findAll();
        return new ResponseEntity<List<Autoridad>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Autoridad> getById(@PathVariable("id") Long id) {
        if (!autoridadService.activo(id))
            return new ResponseEntity(new Mensaje("No existe la autoridad"), HttpStatus.NOT_FOUND);
        Autoridad autoridad = autoridadService.findById(id).get();
        return new ResponseEntity<Autoridad>(autoridad, HttpStatus.OK);
    }

    @GetMapping("/detailpersona/{idPersona}")
    public ResponseEntity<List<Autoridad>> getByPersona(@PathVariable("idPersona") Long idPersona) {
        if (!autoridadService.activoByPersonaId(idPersona))
            return new ResponseEntity(new Mensaje("no existe la autoridad"), HttpStatus.NOT_FOUND);
        List<Autoridad> autoridad = autoridadService.findByPersonaId(idPersona).get();
        return new ResponseEntity<>(autoridad, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody AutoridadDto autoridadDto) {

        ResponseEntity<?> respuestaValidaciones = autoridadService.validations(autoridadDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Autoridad autoridad = autoridadService.create(autoridadDto);
            autoridadService.save(autoridad);
            return new ResponseEntity<>(new Mensaje("Autoridad creada correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @GetMapping("/validar/{idPersona}")
    public boolean esValidoParaCrearAutoridad(@PathVariable Long idPersona) {
        return autoridadService.esValidaParaCrearAutoridad(idPersona);
    }

    @PutMapping("/confirmar/{id}")
    public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody AutoridadDto autoridadDto) {

        if (!autoridadService.existsById(id))
            return new ResponseEntity(new Mensaje("la autoridad no existe"), HttpStatus.NOT_FOUND);

        if (autoridadDto.getConfirmado() != null) {
            Autoridad autoridad = autoridadService.findById(id).get();
            autoridad.setConfirmado(autoridadDto.getConfirmado());
            autoridadService.save(autoridad);

            return new ResponseEntity(new Mensaje("asistencial modificado correctamente"), HttpStatus.OK);

        } else {
            return new ResponseEntity<Mensaje>(new Mensaje("debe indicar el valor de confirmado"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!autoridadService.activo(id))
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

    @GetMapping("/isAutoridad/{idPersona}")
    public ResponseEntity<Boolean> isAutoridad(@PathVariable Long idPersona) {
        boolean isAutoridad = autoridadService.isAutoridad(idPersona);
        return new ResponseEntity<>(isAutoridad, HttpStatus.OK);
    }

    @GetMapping("/hasActiveAutoridadLegajo/{idPersona}")
    public ResponseEntity<Boolean> hasActiveAutoridadLegajo(@PathVariable Long idPersona) {
        boolean hasActiveAutoridadLegajo = autoridadService.hasActiveAutoridadLegajo(idPersona);
        return new ResponseEntity<>(hasActiveAutoridadLegajo, HttpStatus.OK);
    }

}
