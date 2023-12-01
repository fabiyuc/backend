package com.guardias.backend.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("/lista")
    public ResponseEntity<List<Revista>> list() {
        List<Revista> list = revistaService.list();
        return new ResponseEntity<List<Revista>>(list, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody RevistaDto revistaDto) {
        
        Revista revista = new Revista();
        revista.setCargaHoraria(revistaDto.getCargaHoraria());
        revista.setAdicional(revistaDto.getAdicional());
        revista.setTipoRevista(revistaDto.getTipoRevista());
        
        revistaService.save(revista);
        return new ResponseEntity(new Mensaje("revista creada"), HttpStatus.OK);
    }

}
