package com.guardias.backend.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.guardias.backend.entity.Asistencial;
import com.guardias.backend.service.AsistencialService;

@RestController
@RequestMapping("/asistencial")
@CrossOrigin(origins = "http://localhost:4200")
public class AsistencialController {
    
    @Autowired
    AsistencialService asistencialService;

     @GetMapping("/lista")
    public ResponseEntity<List<Asistencial>> list() {
        List<Asistencial> list = asistencialService.list();
        return new ResponseEntity<List<Asistencial>>(list, HttpStatus.OK);
    }
}
