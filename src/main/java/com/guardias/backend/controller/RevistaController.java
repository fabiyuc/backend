package com.guardias.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Revista;
import com.guardias.backend.service.RevistaService;

@RestController
@RequestMapping("/revista")
@CrossOrigin(origins = "http://localhost:4200")
public class RevistaController {

    @Autowired
    RevistaService revistaService;

    @GetMapping("/lista")
    public ResponseEntity<List<Revista>> list() {
        List<Revista> list = revistaService.list();
        return new ResponseEntity<List<Revista>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Revista> getById(@PathVariable("id") Long id) {
        if (!revistaService.existsById(id))
            return new ResponseEntity(new Mensaje("No existe la situacion de revista"), HttpStatus.NOT_FOUND);
        Revista revista = revistaService.getOne(id).get();
        return new ResponseEntity<Revista>(revista, HttpStatus.OK);
    }

    @GetMapping("/detail/{nombre}")
    public ResponseEntity<Revista> getByNombre(@PathVariable("nombre") String nombre) {
        if (!revistaService.existsByNombre(nombre))
            return new ResponseEntity(new Mensaje("No existe la situacion de revista"), HttpStatus.NOT_FOUND);
        Revista revista = revistaService.getByNombre(nombre).get();
        return new ResponseEntity<Revista>(revista, HttpStatus.OK);
    }

    // TODO (RevistaController create)
    // TODO (RevistaController update)

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {

        if (!revistaService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el revista"), HttpStatus.NOT_FOUND);
        revistaService.deleteById(id);
        return new ResponseEntity(new Mensaje("revista eliminado"), HttpStatus.OK);
    }

}
