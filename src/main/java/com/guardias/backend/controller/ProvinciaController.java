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
import com.guardias.backend.entity.Provincia;
import com.guardias.backend.service.ProvinciaService;

@RestController
@RequestMapping("/provincia")
@CrossOrigin(origins = "http://localhost:4200")
public class ProvinciaController {

    @Autowired
    ProvinciaService provinciaService;

    @GetMapping("/lista")
    public ResponseEntity<List<Provincia>> list() {
        List<Provincia> list = provinciaService.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<List<Provincia>> getById(@PathVariable("id") Long id) {
        if (!provinciaService.existById(id))
            return new ResponseEntity(new Mensaje("provincia no existe"), HttpStatus.NOT_FOUND);
        Provincia provincia = provinciaService.getById(id).get();
        return new ResponseEntity(provincia, HttpStatus.OK);
    }

    // TODO (ProvinciaController create)
    // TODO (ProvinciaController update)
    // TODO (ProvinciaController delete)
    // TODO (ProvinciaController detalle por nombre)
}
