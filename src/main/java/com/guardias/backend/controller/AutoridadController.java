package com.guardias.backend.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

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

    @GetMapping("/detailnombre/{nombre}")
    public ResponseEntity<Autoridad> getByNombre(@PathVariable("nombre") String nombre) {
        if (!autoridadService.activoByNombre(nombre))
            return new ResponseEntity(new Mensaje("No existe la autoridad"), HttpStatus.NOT_FOUND);
        Autoridad autoridad = autoridadService.findByNombre(nombre).get();
        return new ResponseEntity(autoridad, HttpStatus.OK);
    }

    @GetMapping("/list/{fechaInicio}")
    public ResponseEntity<List<Autoridad>> getByFechainicio(
            @PathVariable("fechaInicio") LocalDate fechaInicio) {
        List<Autoridad> list = autoridadService.findByFechaInicio(fechaInicio).get();
        return new ResponseEntity<List<Autoridad>>(list, HttpStatus.OK);
    }

    @GetMapping("/detailefector/{idEfector}")
    public ResponseEntity<List<Autoridad>> getByEfector(@PathVariable("idEfector") Long idEfector) {
        if (!autoridadService.activoByEfectorId(idEfector))
            return new ResponseEntity(new Mensaje("no existe la autoridad"), HttpStatus.NOT_FOUND);
        List<Autoridad> autoridad = autoridadService.findByEfectorId(idEfector).get();
        return new ResponseEntity<>(autoridad, HttpStatus.OK);
    }

    @GetMapping("/detailpersona/{idPersona}")
    public ResponseEntity<List<Autoridad>> getByPersona(@PathVariable("idPersona") Long idPersona) {
        if (!autoridadService.activoByPersonaId(idPersona))
            return new ResponseEntity(new Mensaje("no existe la autoridad"), HttpStatus.NOT_FOUND);
        List<Autoridad> autoridad = autoridadService.findByPersonaId(idPersona).get();
        return new ResponseEntity<>(autoridad, HttpStatus.OK);
    }

    public ResponseEntity<?> validations(AutoridadDto autoridadDto, Long id) {
        if (autoridadDto.getNombre() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("El Nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        if (autoridadDto.getFechaInicio() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("La fecha de inicio es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (autoridadDto.getIdEfector() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("El efector es obligatorio"), HttpStatus.BAD_REQUEST);

        if (autoridadDto.getIdPersona() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("la persona es obligatoria"), HttpStatus.BAD_REQUEST);

        if (autoridadService.activoByNombre(autoridadDto.getNombre())
                && (autoridadService.findByNombre(autoridadDto.getNombre()).get().getId() != id))
            return new ResponseEntity<Mensaje>(new Mensaje("El Nombre ya existe"), HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    public Autoridad createUpdate(Autoridad autoridad, AutoridadDto autoridadDto) {

        if (autoridadDto.getNombre() != null &&
                !autoridadDto.getNombre().isEmpty()
                && !autoridadDto.getNombre().equals(autoridad.getNombre()))
            autoridad.setNombre(autoridadDto.getNombre());

        if (autoridadDto.getFechaInicio() != null && !autoridadDto.getFechaInicio().equals(autoridad.getFechaInicio()))
            autoridad.setFechaInicio(autoridadDto.getFechaInicio());

        if (autoridad.getFechaFinal() != autoridadDto.getFechaFinal() && autoridadDto.getFechaFinal() != null)
            autoridad.setFechaFinal(autoridadDto.getFechaFinal());

        autoridad.setEsRegional(autoridadDto.isEsRegional());

        if (autoridad.getEfector() == null ||
                (autoridadDto.getIdEfector() != null &&
                        !Objects.equals(autoridad.getEfector().getId(),
                                autoridadDto.getIdEfector()))) {
            autoridad.setEfector(efectorService.findById(autoridadDto.getIdEfector()));
        }

        if (autoridad.getPersona() == null ||
                (autoridadDto.getIdPersona() != null &&
                        !Objects.equals(autoridad.getPersona().getId(),
                                autoridadDto.getIdPersona()))) {
            autoridad.setPersona(personService.findById(autoridadDto.getIdPersona()));
        }
        autoridad.setActivo(true);

        return autoridad;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody AutoridadDto autoridadDto) {

        // Verificar si la persona tiene una autoridad activa
        if (autoridadService.tieneAutoridadActiva(autoridadDto.getIdPersona())) {
            return new ResponseEntity<>(new Mensaje("La persona tiene una autoridad activa"), HttpStatus.BAD_REQUEST);
        }
        ResponseEntity<?> respuestaValidaciones = validations(autoridadDto, 0L);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Autoridad autoridad = createUpdate(new Autoridad(), autoridadDto);
            autoridad.setEsActual(true);
            autoridadService.save(autoridad);
            return new ResponseEntity<>(new Mensaje("Autoridad creada correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody AutoridadDto autoridadDto) {
        // Verificar si la autoridad existe y está activa
        if (!autoridadService.activo(id))
            return new ResponseEntity(new Mensaje("No existe la autoridad"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = validations(autoridadDto, id);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Autoridad autoridad = createUpdate(autoridadService.findById(id).get(), autoridadDto);
            autoridad.setEsActual(autoridadDto.isEsActual());
            autoridadService.save(autoridad);
            return new ResponseEntity<>(new Mensaje("Autoridad modificada correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
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
}
