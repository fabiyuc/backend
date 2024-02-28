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

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.RevistaDto;
import com.guardias.backend.entity.Revista;
import com.guardias.backend.service.RevistaService;

@RestController
@RequestMapping("/revista")
@CrossOrigin(origins = "http://localhost:4200")
public class RevistaController {

    @Autowired
    RevistaService revistaService;

    @GetMapping("/list")
    public ResponseEntity<List<Revista>> list() {
        List<Revista> list = revistaService.findByActivo(true);
        return new ResponseEntity<List<Revista>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Revista>> listAll() {
        List<Revista> list = revistaService.findAll();
        return new ResponseEntity<List<Revista>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Revista> getById(@PathVariable("id") Long id) {
        if (!revistaService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la revista con ese ID"), HttpStatus.NOT_FOUND);
        Revista revista = revistaService.findById(id).get();
        return new ResponseEntity<Revista>(revista, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody RevistaDto revistaDto) {

        if (revistaDto.getTipoRevista() == null)
            return new ResponseEntity(new Mensaje("indicar el tipo de revista"),
                    HttpStatus.BAD_REQUEST);

        if (revistaDto.getCategoria() == null)
            return new ResponseEntity(new Mensaje("indicar la categoria"),
                    HttpStatus.BAD_REQUEST);

        if (revistaDto.getAdicional() == null)
            return new ResponseEntity(new Mensaje("indicar el adicional"),
                    HttpStatus.BAD_REQUEST);

        if (revistaDto.getCargaHoraria() == null)
            return new ResponseEntity(new Mensaje("indicar la carga horaria"),
                    HttpStatus.BAD_REQUEST);

        Revista revista = new Revista();
        revista.setCargaHoraria(revistaDto.getCargaHoraria());
        revista.setCategoria(revistaDto.getCategoria());
        revista.setAdicional(revistaDto.getAdicional());
        revista.setTipoRevista(revistaDto.getTipoRevista());

        revistaService.save(revista);
        return new ResponseEntity(new Mensaje("revista creada"), HttpStatus.OK);
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody RevistaDto revistaDto) {
        if (!revistaService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la revista"), HttpStatus.NOT_FOUND);

        if (revistaDto.getTipoRevista() == null)
            return new ResponseEntity(new Mensaje("indicar el tipo de revista"),
                    HttpStatus.BAD_REQUEST);

        if (revistaDto.getCategoria() == null)
            return new ResponseEntity(new Mensaje("indicar la categoria"),
                    HttpStatus.BAD_REQUEST);

        if (revistaDto.getAdicional() == null)
            return new ResponseEntity(new Mensaje("indicar el adicional"),
                    HttpStatus.BAD_REQUEST);

        if (revistaDto.getCargaHoraria() == null)
            return new ResponseEntity(new Mensaje("indicar la carga horaria"),
                    HttpStatus.BAD_REQUEST);

        Revista revista = revistaService.findById(id).get();
        revista.setCargaHoraria(revistaDto.getCargaHoraria());
        revista.setAdicional(revistaDto.getAdicional());
        revista.setCategoria(revistaDto.getCategoria());
        revista.setTipoRevista(revistaDto.getTipoRevista());
        revistaService.save(revista);
        return new ResponseEntity(new Mensaje("revista actualizada"), HttpStatus.OK);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!revistaService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la revista"), HttpStatus.NOT_FOUND);

        Revista revista = revistaService.findById(id).get();
        revista.setActivo(false);
        revistaService.save(revista);
        return new ResponseEntity(new Mensaje("revista eliminada correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!revistaService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la revista"), HttpStatus.NOT_FOUND);
        revistaService.deleteById(id);
        return new ResponseEntity(new Mensaje("revista eliminada FISICAMENTE"), HttpStatus.OK);
    }
}
