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

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/autoridad")
@CrossOrigin(origins = "http://localhost:4200")
public class AutoridadController {

    @Autowired
    AutoridadService autoridadService;

    @GetMapping("/list")
    public ResponseEntity<List<Autoridad>> list() {
        List<Autoridad> list = autoridadService.findByActivoTrue();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Autoridad>> listAll() {
        List<Autoridad> list = autoridadService.findAll();
        return new ResponseEntity<List<Autoridad>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<Autoridad>> getById(@PathVariable("id") Long id) {
        if (!autoridadService.existsById(id))
            return new ResponseEntity(new Mensaje("No existe la autoridad"), HttpStatus.NOT_FOUND);
        Autoridad autoridad = autoridadService.findById(id).get();
        return new ResponseEntity(autoridad, HttpStatus.OK);
    }

    @GetMapping("/detailnombre/{nombre}")
    public ResponseEntity<List<Autoridad>> getByNombre(@PathVariable("nombre") String nombre) {
        if (!autoridadService.existsByNombre(nombre))
            return new ResponseEntity(new Mensaje("No existe la autoridad"), HttpStatus.NOT_FOUND);
        Autoridad autoridad = autoridadService.findByNombre(nombre).get();
        return new ResponseEntity(autoridad, HttpStatus.OK);
    }

    // TODO hacer las validaciones y el createUpdate
    public ResponseEntity<?> validations(AutoridadDto autoridadDto) {
        if (StringUtils.isBlank(autoridadDto.getNombre()))
            return new ResponseEntity<>(new Mensaje("El Nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        if (autoridadService.existsByNombre(autoridadDto.getNombre()))
            return new ResponseEntity<>(new Mensaje("El Nombre ya existe"), HttpStatus.BAD_REQUEST);

        if (autoridadDto.getFechaInicio() == null)
            return new ResponseEntity<>(new Mensaje("La fecha de inicio es obligatoria"), HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    private Autoridad createUpdate(Autoridad autoridad, AutoridadDto autoridadDto) {
        if (autoridad.getNombre() != autoridadDto.getNombre() && autoridadDto.getNombre() != null)
            autoridad.setNombre(autoridadDto.getNombre());

        if (autoridad.getFechaInicio() != autoridadDto.getFechaInicio() && autoridadDto.getFechaInicio() != null)
            autoridad.setFechaInicio(autoridadDto.getFechaInicio());

        if (autoridad.getFechaFinal() != autoridadDto.getFechaFinal() && autoridadDto.getFechaFinal() != null)
            autoridad.setFechaFinal(autoridadDto.getFechaFinal());

        autoridad.setEsActual(autoridadDto.isEsActual());

        autoridad.setEsRegional(autoridadDto.isEsRegional());

        if (autoridad.getEfector() != autoridadDto.getEfector() && autoridadDto.getEfector() != null)
            autoridad.setEfector(autoridadDto.getEfector());

        if (autoridad.getPersona() != autoridadDto.getPersona() && autoridadDto.getPersona() != null)
            autoridad.setPersona(autoridadDto.getPersona());

        return autoridad;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody AutoridadDto autoridadDto) {

        ResponseEntity<?> respuestaValidaciones = validations(autoridadDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Autoridad autoridad = createUpdate(new Autoridad(), autoridadDto);
            autoridad.setActivo(true);
            autoridadService.save(autoridad);

            return new ResponseEntity<>(new Mensaje("asistencial creado"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody AutoridadDto autoridadDto) {
        if (!autoridadService.activo(id))
            return new ResponseEntity(new Mensaje("no existe la autoridad"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = validations(autoridadDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Autoridad autoridad = createUpdate(autoridadService.findById(id).get(), autoridadDto);
            autoridadService.save(autoridad);
            return new ResponseEntity<>(new Mensaje("asistencial actualizado "), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PostMapping("/{idAutoridad}/addEfector/{idEfector}")
    public ResponseEntity<?> agregarEfector(@PathVariable("idAutoridad") Long idAutoridad,
            @PathVariable("idEfector") Long idEfector) {
        try {
            autoridadService.agregarEfector(idAutoridad, idEfector);
            return new ResponseEntity<>(new Mensaje("Efector agregado"), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(new Mensaje("No se pudo agregar el efector"), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new Mensaje("Error al agregar el efector"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{idAutoridad}/addPersona/{idPersona}")
    public ResponseEntity<?> agregarPersona(@PathVariable("idAutoridad") Long idAutoridad,

            @PathVariable("idPersona") Long idPersona) {
        try {
            autoridadService.agregarPersona(idAutoridad, idPersona);
            return new ResponseEntity<>(new Mensaje("Persona agregada"), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(new Mensaje("No se pudo agregar la persona"),
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new Mensaje("Error al agregar la persona"),
                    HttpStatus.BAD_REQUEST);
        }
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
