package com.guardias.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Efector;
import com.guardias.backend.service.EfectorService;

@Controller
@RequestMapping("/efector")
@CrossOrigin(origins = "http://localhost:4200")
public class EfectorController {

    @Autowired
    EfectorService efectorService;

    @GetMapping("/lista")
    public ResponseEntity<List<Efector>> list() {
        List<Efector> list = efectorService.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<List<Efector>> getById(@PathVariable("id") Long id) {
        if (!efectorService.existById(id))
            return new ResponseEntity(new Mensaje("Efector no encontrao"), HttpStatus.NOT_FOUND);
        Efector efector = efectorService.getById(id).get();
        return new ResponseEntity(efector, HttpStatus.OK);
    }

}
