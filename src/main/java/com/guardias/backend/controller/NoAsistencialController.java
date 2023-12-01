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
import com.guardias.backend.entity.NoAsistencial;
import com.guardias.backend.service.NoAsistencialService;

@RestController
@RequestMapping("/noasistencial")
@CrossOrigin(origins = "http://localhost:4200")
public class NoAsistencialController {

    @Autowired
    NoAsistencialService noAsistencialService;

    @GetMapping("/lista")
    public ResponseEntity<List<NoAsistencial>> list() {
        List<NoAsistencial> list = noAsistencialService.list();
        return new ResponseEntity<List<NoAsistencial>>(list, HttpStatus.OK);
    }

    // TODO (NoAsistencialController create)
    // TODO (NoAsistencialController update)

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {

        if (!noAsistencialService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el noAsistencial"), HttpStatus.NOT_FOUND);
        noAsistencialService.deleteById(id);
        return new ResponseEntity(new Mensaje("noAsistencial eliminado"), HttpStatus.OK);
    }
}
