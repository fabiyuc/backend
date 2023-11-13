package com.guardias.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Pais;
import com.guardias.backend.service.PaisService;

@RestController
@RequestMapping("/pais")
@CrossOrigin(origins = "http://localhost:4200")
public class PaisController {

    @Autowired
    PaisService paisService;

    @GetMapping("/lista")
    public ResponseEntity<List<Pais>> list() {
        List<Pais> list = paisService.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<List<Pais>> getById(@PathVariable("id") Long id) {
        if (!paisService.existById(id))
            return new ResponseEntity(new Mensaje("pais no existe"), HttpStatus.NOT_FOUND);
        Pais pais = paisService.getById(id).get();
        return new ResponseEntity(pais, HttpStatus.OK);
    }
}