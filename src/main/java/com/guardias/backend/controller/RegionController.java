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
import com.guardias.backend.entity.Region;
import com.guardias.backend.service.RegionService;

@RestController
@RequestMapping("/region")
@CrossOrigin(origins = "http://localhost:4200")
public class RegionController {

    @Autowired
    RegionService regionService;

    @GetMapping("/lista")
    public ResponseEntity<List<Region>> list() {
        List<Region> list = regionService.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<List<Region>> getById(@PathVariable("id") int id) {
        if (!regionService.existById(id))
            return new ResponseEntity(new Mensaje("region no existe"), HttpStatus.NOT_FOUND);
        Region region = regionService.getById(id).get();
        return new ResponseEntity(region, HttpStatus.OK);
    }
}
