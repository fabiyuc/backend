package com.guardias.backend.controller;

import java.util.ArrayList;
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
import com.guardias.backend.entity.ValorGuardiaCargoYagrup;
import com.guardias.backend.service.ValorGuardiaCargoYagrupService;

@RestController
@RequestMapping("/valorGuardiaCargoYagrup")
@CrossOrigin(origins = "http://localhost:4200")
public class ValorGuardiaCargoYagrupController {

    @Autowired
    ValorGuardiaCargoYagrupService valorGuardiaCargoYagrupService;

    @GetMapping("/list")
    public ResponseEntity<List<ValorGuardiaCargoYagrup>> list() {
        List<ValorGuardiaCargoYagrup> valorGuardiaCyAList = valorGuardiaCargoYagrupService.findByActivoTrue()
                .orElse(new ArrayList<>());

        return new ResponseEntity<List<ValorGuardiaCargoYagrup>>(valorGuardiaCyAList, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<ValorGuardiaCargoYagrup>> listAll() {
        List<ValorGuardiaCargoYagrup> list = valorGuardiaCargoYagrupService.findAll();
        return new ResponseEntity<List<ValorGuardiaCargoYagrup>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ValorGuardiaCargoYagrup> getById(@PathVariable("id") Long id) {
        if (!valorGuardiaCargoYagrupService.activo(id))
            return new ResponseEntity(new Mensaje("No existe el valor de guardia"), HttpStatus.NOT_FOUND);
        ValorGuardiaCargoYagrup valorGuardiaCyA = valorGuardiaCargoYagrupService.findById(id).get();
        return new ResponseEntity<ValorGuardiaCargoYagrup>(valorGuardiaCyA, HttpStatus.OK);
    }
}
