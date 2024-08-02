package com.guardias.backend.security.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guardias.backend.security.entity.Rol;
import com.guardias.backend.security.service.RolService;

@RestController
@RequestMapping("/rol")
@CrossOrigin(origins = "http://localhost:4200")
public class RolController {

    @Autowired
    RolService rolService;

    @GetMapping("/list")
    public ResponseEntity<List<Rol>> list() {
        List<Rol> list = rolService.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    
}
