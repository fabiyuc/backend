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
import com.guardias.backend.dto.AdicionalDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Adicional;
import com.guardias.backend.service.AdicionalService;

@RestController
@RequestMapping("/adicional")
@CrossOrigin(origins = "http://localhost:4200")
public class AdicionalController {

    @Autowired
    AdicionalService adicionalService;

    @GetMapping("/lista")
    public ResponseEntity<List<Adicional>> list() {
        List<Adicional> list = adicionalService.list();
        return new ResponseEntity<List<Adicional>>(list, HttpStatus.OK);
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<Adicional> getById(@PathVariable("id") Long id) {
        if (!adicionalService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe adicional con ese nombre"), HttpStatus.NOT_FOUND);
        Adicional adicional = adicionalService.getById(id).get();
        return new ResponseEntity<Adicional>(adicional, HttpStatus.OK);

    }

    @GetMapping("/detallenombre/{nombre}")
    public ResponseEntity<Adicional> getByNombre(@PathVariable("nombre") String nombre) {
        if (!adicionalService.existsByNombre(nombre))
            return new ResponseEntity(new Mensaje("no existe adicional con ese nombre"), HttpStatus.NOT_FOUND);
        Adicional adicional = adicionalService.getByNombre(nombre).get();
        return new ResponseEntity<Adicional>(adicional, HttpStatus.OK);

    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody AdicionalDto adicionalDto) {
        if (StringUtils.isBlank(adicionalDto.getNombre()))
            return new ResponseEntity<>(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        if (adicionalService.existsByNombre(adicionalDto.getNombre()))
            return new ResponseEntity(new Mensaje("ese nombre ya existe"), HttpStatus.BAD_REQUEST);
        Adicional adicional = new Adicional();
        adicional.setNombre(adicionalDto.getNombre());
        adicionalService.save(adicional);
        return new ResponseEntity<>(new Mensaje("Adicional creado"), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody AdicionalDto adicionalDto) {
        if (!adicionalService.existsById(id))
            return new ResponseEntity(new Mensaje("El adicional no existe"), HttpStatus.NOT_FOUND);

        if (adicionalService.existsByNombre(adicionalDto.getNombre())
                && adicionalService.getByNombre(adicionalDto.getNombre()).get().getId() == id)
            return new ResponseEntity(new Mensaje("ese nombre ya existe"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(adicionalDto.getNombre()))
            return new ResponseEntity<>(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        Adicional adicional = adicionalService.getById(id).get();
        adicional.setNombre(adicionalDto.getNombre());
        adicionalService.save(adicional);
        return new ResponseEntity<>(new Mensaje("Adicional Actualizado"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        if (!adicionalService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        adicionalService.delete(id);
        return new ResponseEntity<>(new Mensaje("Adicional eliminado"), HttpStatus.OK);

    }

}
// 3W9UxKZRVT