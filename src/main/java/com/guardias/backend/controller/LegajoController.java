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

import com.guardias.backend.dto.LegajoDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Legajo;
import com.guardias.backend.service.LegajoService;

@RestController
@RequestMapping("/legajo")
@CrossOrigin(origins = "http://localhost:4200")
public class LegajoController {

    @Autowired
    LegajoService legajoService;

    @GetMapping("/list")
    public ResponseEntity<List<Legajo>> list() {
        List<Legajo> list = legajoService.list();
        return new ResponseEntity<List<Legajo>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Legajo> getById(@PathVariable("id") Long id) {
        if (!legajoService.existsById(id))
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

        if (legajoDto.getPersona() == null)
            return new ResponseEntity(new Mensaje("indicar la persona"),
                    HttpStatus.BAD_REQUEST);

        if (legajoDto.getUdo() == null)
            return new ResponseEntity(new Mensaje("indicar la UdO"),
                    HttpStatus.BAD_REQUEST);

        if (legajoDto.getProfesion() == null)
            return new ResponseEntity(new Mensaje("indicar la profesion"),
                    HttpStatus.BAD_REQUEST);
        if (legajoDto.getRevista() == null)
            return new ResponseEntity(new Mensaje("indicar la situacion de revista"),
                    HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    private Legajo createUpdate(Legajo legajo, LegajoDto legajoDto) {
        if (!legajoDto.getFechaInicio().equals(legajo.getFechaInicio()))
            legajo.setFechaInicio(legajoDto.getFechaInicio());
        if (!legajoDto.getFechaFinal().equals(legajo.getFechaFinal()))
            legajo.setFechaFinal(legajoDto.getFechaFinal());
        if (!legajoDto.getPersona().equals(legajo.getPersona()))
            legajo.setPersona(legajoDto.getPersona());
        if (!legajoDto.getUdo().equals(legajo.getUdo()))
            legajo.setUdo(legajoDto.getUdo());
        if (!legajoDto.getActual().equals(legajo.getActual()))
            legajo.setActual(legajoDto.getActual());
        if (!legajoDto.getLegal().equals(legajo.getLegal()))
            legajo.setLegal(legajoDto.getLegal());
        if (!legajoDto.getMatriculaNacional().equals(legajo.getMatriculaNacional()))
            legajo.setMatriculaNacional(legajoDto.getMatriculaNacional());
        if (!legajoDto.getMatriculaProvincial().equals(legajo.getMatriculaProvincial()))
            legajo.setMatriculaProvincial(legajoDto.getMatriculaProvincial());
        if (!legajoDto.getProfesion().equals(legajo.getProfesion()))
            legajo.setProfesion(legajoDto.getProfesion());
        if (!legajoDto.getSuspencion().equals(legajo.getSuspencion()))
            legajo.setSuspencion(legajoDto.getSuspencion());
        if (!legajoDto.getRevista().equals(legajo.getRevista()))
            legajo.setRevista(legajoDto.getRevista());
        if (!legajoDto.getPersona().equals(legajo.getPersona()))
            legajo.setPersona(legajoDto.getPersona());
        if (!legajoDto.getCargo().equals(legajo.getCargo()))
            legajo.setCargo(legajoDto.getCargo());
        if (!legajoDto.getEfectores().equals(legajo.getEfectores()))
            legajo.setEfectores(legajoDto.getEfectores());

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
            return new ResponseEntity(new Mensaje("error al guardar los cambios"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody LegajoDto legajoDto) {
        if (!legajoService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el legajo"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = validations(legajoDto);
        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Legajo legajo = createUpdate(legajoService.findById(id).get(), legajoDto);
            legajoService.save(legajo);

            return new ResponseEntity(new Mensaje("Legajo creado"), HttpStatus.OK);
        } else {
            return new ResponseEntity(new Mensaje("error al guardar los cambios"),
                    HttpStatus.BAD_REQUEST);
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
