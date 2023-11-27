package com.guardias.backend.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import com.guardias.backend.dto.ProfesionDto;
import com.guardias.backend.entity.Profesion;
import com.guardias.backend.service.ProfesionService;

@RestController
@RequestMapping("/profesion")
@CrossOrigin(origins = "http://localhost:4200")
public class ProfesionController {

    @Autowired
    ProfesionService profesionService;

    @GetMapping("/lista")
    public ResponseEntity<List<Profesion>> list() {
        List<Profesion> list = profesionService.list();
        return new ResponseEntity<List<Profesion>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Profesion> getById(@PathVariable("id") Long id) {
        if (!profesionService.existsById(id))
            return new ResponseEntity(new Mensaje("No existe la profesion"), HttpStatus.NOT_FOUND);
        Profesion profesion = profesionService.getOne(id).get();
        return new ResponseEntity<Profesion>(profesion, HttpStatus.OK);
    }

    @GetMapping("/detailnombre/{nombre}")
    public ResponseEntity<Profesion> getByNombre(@PathVariable("nombre") String nombre) {
        if (!profesionService.existsByNombre(nombre))
            return new ResponseEntity(new Mensaje("no existe la profesion"), HttpStatus.NOT_FOUND);
        Profesion profesion = profesionService.getByNombre(nombre).get();
        return new ResponseEntity<Profesion>(profesion, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ProfesionDto profesionDto) {
        if (StringUtils.isBlank(profesionDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"),
                    HttpStatus.BAD_REQUEST);
        if (profesionService.existsByNombre(profesionDto.getNombre()))
            return new ResponseEntity(new Mensaje("ese nombre ya existe"),
                    HttpStatus.BAD_REQUEST);
        Profesion profesion = new Profesion(profesionDto.getNombre(),
                profesionDto.getEsAsistencial());
        profesionService.save(profesion);
        return new ResponseEntity(new Mensaje("Profesion creada"), HttpStatus.OK);
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody ProfesionDto profesionDto) {
        if (!profesionService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la profesion"), HttpStatus.NOT_FOUND);

        if (profesionService.existsByNombre(profesionDto.getNombre()) &&
                profesionService.getByNombre(profesionDto.getNombre()).get().getId() == id)
            return new ResponseEntity(new Mensaje("esa profesion ya existe"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(profesionDto.getNombre()))
            return new ResponseEntity(new Mensaje("la profesion es obligatoria"), HttpStatus.BAD_REQUEST);

        // ********** ver de validar que las matriculas no sean nulas en el front? o
        // completar aqui mejor**********

        Profesion profesion = profesionService.getOne(id).get();
        if (profesion.getNombre() != profesionDto.getNombre() && profesionDto.getNombre() != null
                && !profesionDto.getNombre().isEmpty())
            profesion.setNombre(profesionDto.getNombre());
        if (profesion.getEsAsistencial() != profesionDto.getEsAsistencial())
            profesion.setEsAsistencial(profesionDto.getEsAsistencial());
        profesionService.save(profesion);
        return new ResponseEntity(new Mensaje("Profesion actualizada"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {

        if (!profesionService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la profesion"), HttpStatus.NOT_FOUND);
        profesionService.delete(id);
        return new ResponseEntity(new Mensaje("Profesion eliminada"), HttpStatus.OK);
    }
}
