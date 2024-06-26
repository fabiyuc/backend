package com.guardias.backend.controller;

import java.util.ArrayList;
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

import com.guardias.backend.dto.LegajoDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Efector;
import com.guardias.backend.entity.Legajo;
import com.guardias.backend.service.CargoService;
import com.guardias.backend.service.EfectorService;
import com.guardias.backend.service.LegajoService;
import com.guardias.backend.service.PersonService;
import com.guardias.backend.service.ProfesionService;
import com.guardias.backend.service.RevistaService;
import com.guardias.backend.service.SuspencionService;

@RestController
@RequestMapping("/legajo")
@CrossOrigin(origins = "http://localhost:4200")
public class LegajoController {

    @Autowired
    LegajoService legajoService;
    @Autowired
    PersonService personService;
    @Autowired
    EfectorService efectorService;
    @Autowired
    ProfesionService profesionService;
    @Autowired
    SuspencionService suspencionService;
    @Autowired
    RevistaService revistaService;
    @Autowired
    CargoService cargoService;

    @GetMapping("/list")
    public ResponseEntity<List<Legajo>> list() {
        List<Legajo> list = legajoService.findByActivo();
        return new ResponseEntity<List<Legajo>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Legajo>> listAll() {
        List<Legajo> list = legajoService.findAll();
        return new ResponseEntity<List<Legajo>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Legajo> getById(@PathVariable("id") Long id) {
        if (!legajoService.activo(id))
            return new ResponseEntity(new Mensaje("No existe el legajo"),
                    HttpStatus.NOT_FOUND);
        Legajo legajo = legajoService.findById(id).get();
        return new ResponseEntity<Legajo>(legajo, HttpStatus.OK);
    }

    private ResponseEntity<?> validations(LegajoDto legajoDto) {
        if (legajoDto.getFechaInicio() == null) {
            return new ResponseEntity(new Mensaje("La fecha de inicio es obligatoria"), HttpStatus.BAD_REQUEST);
        }

        if (legajoDto.getActual() == null)
            return new ResponseEntity(new Mensaje("indicar si es actual o no"),
                    HttpStatus.BAD_REQUEST);
        if (legajoDto.getLegal() == null)
            return new ResponseEntity(new Mensaje("indicar si es legal o no"),
                    HttpStatus.BAD_REQUEST);

        if (legajoDto.getIdPersona() == null)
            return new ResponseEntity(new Mensaje("indicar la persona"),
                    HttpStatus.BAD_REQUEST);

        if (legajoDto.getIdUdo() == null)
            return new ResponseEntity(new Mensaje("indicar la UdO"),
                    HttpStatus.BAD_REQUEST);

        if (legajoDto.getIdProfesion() == null)
            return new ResponseEntity(new Mensaje("indicar la profesion"),
                    HttpStatus.BAD_REQUEST);
        if (legajoDto.getIdRevista() == null)
            return new ResponseEntity(new Mensaje("indicar la situacion de revista"),
                    HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    private Legajo createUpdate(Legajo legajo, LegajoDto legajoDto) {

        if (legajo.getFechaInicio() != null && (legajo.getFechaInicio() != legajoDto.getFechaInicio())
                && legajoDto.getFechaInicio() != null)
            legajo.setFechaInicio(legajoDto.getFechaInicio());

        if (legajo.getFechaFinal() != null && (legajo.getFechaFinal() != legajoDto.getFechaFinal())
                && legajoDto.getFechaFinal() != null)
            legajo.setFechaFinal(legajoDto.getFechaFinal());

        if (legajo.getPersona() == null ||
                (legajoDto.getIdPersona() != null &&
                        !Objects.equals(legajo.getPersona().getId(),
                                legajoDto.getIdPersona()))) {
            legajo.setPersona(personService.findById(legajoDto.getIdPersona()));
        }

        if (legajo.getUdo() == null ||
                (legajoDto.getIdUdo() != null &&
                        !Objects.equals(legajo.getUdo().getId(),
                                legajoDto.getIdUdo()))) {
            legajo.setUdo(efectorService.findById(legajoDto.getIdUdo()));
        }

        legajo.setActual(legajoDto.getActual());
        legajo.setLegal(legajoDto.getLegal());

        if (legajo.getMatriculaNacional() != null && (legajo.getMatriculaNacional() != legajoDto.getMatriculaNacional())
                && legajoDto.getMatriculaNacional() != null)
            legajo.setMatriculaNacional(legajoDto.getMatriculaNacional());

        if (legajo.getMatriculaProvincial() != null
                && (legajo.getMatriculaProvincial() != legajoDto.getMatriculaProvincial())
                && legajoDto.getMatriculaProvincial() != null)
            legajo.setMatriculaProvincial(legajoDto.getMatriculaProvincial());

        if (legajo.getProfesion() == null ||
                (legajoDto.getIdProfesion() != null &&
                        !Objects.equals(legajo.getProfesion().getId(),
                                legajoDto.getIdProfesion()))) {
            legajo.setProfesion(profesionService.findById(legajoDto.getIdProfesion()).get());
        }

        if (legajo.getSuspencion() == null ||
                (legajoDto.getIdSuspencion() != null &&
                        !Objects.equals(legajo.getSuspencion().getId(),
                                legajoDto.getIdSuspencion()))) {
            legajo.setSuspencion(suspencionService.findById(legajoDto.getIdSuspencion()).get());
        }

        if (legajo.getRevista() == null ||
                (legajoDto.getIdRevista() != null &&
                        !Objects.equals(legajo.getRevista().getId(),
                                legajoDto.getIdRevista()))) {
            legajo.setRevista(revistaService.findById(legajoDto.getIdRevista()).get());
        }

        if (legajo.getCargo() == null ||
                (legajoDto.getIdCargo() != null &&
                        !Objects.equals(legajo.getCargo().getId(),
                                legajoDto.getIdCargo()))) {
            legajo.setCargo(cargoService.findById(legajoDto.getIdCargo()).get());
        }

        if (legajoDto.getIdEfectores() != null) {
            List<Long> idList = new ArrayList();
            if (legajo.getEfectores() != null) {
                for (Efector efector : legajo.getEfectores()) {
                    for (Long id : legajoDto.getIdEfectores()) {
                        if (!efector.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            }

            List<Long> idsToAdd = idList.isEmpty() ? legajoDto.getIdEfectores() : idList;
            for (Long id : idsToAdd) {
                legajo.getEfectores().add(efectorService.findById(id));
                efectorService.findById(id).getLegajos().add(legajo);
            }
        }

        return legajo;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody LegajoDto legajoDto) {
        ResponseEntity<?> respuestaValidaciones = validations(legajoDto);
        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Legajo legajo = createUpdate(new Legajo(), legajoDto);
            legajoService.save(legajo);

            return new ResponseEntity(new Mensaje("Legajo creado"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody LegajoDto legajoDto) {
        if (!legajoService.activo(id))
            return new ResponseEntity(new Mensaje("no existe el legajo"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = validations(legajoDto);
        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Legajo legajo = createUpdate(legajoService.findById(id).get(), legajoDto);
            legajoService.save(legajo);

            return new ResponseEntity(new Mensaje("Legajo creado"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {

        if (!legajoService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el legajo"), HttpStatus.NOT_FOUND);

        Legajo legajo = legajoService.findById(id).get();
        legajo.setActivo(false);
        legajoService.save(legajo);
        return new ResponseEntity(new Mensaje("legajo eliminado"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") Long id) {

        if (!legajoService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el legajo"), HttpStatus.NOT_FOUND);
        legajoService.deleteById(id);
        return new ResponseEntity(new Mensaje("legajo eliminado FISICAMENTE"), HttpStatus.OK);
    }

}
