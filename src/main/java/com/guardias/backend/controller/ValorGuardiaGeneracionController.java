package com.guardias.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.valoresGuardias.GenerarValoresGuardiaDto;
import com.guardias.backend.service.ValorGuardiaGeneracionService;

@RestController
@RequestMapping("/valoresGuardia")
@CrossOrigin(origins = "http://localhost:4200")
public class ValorGuardiaGeneracionController {
    @Autowired
    ValorGuardiaGeneracionService valorGuardiaGeneracionService;

    @PostMapping("/generar")
    public ResponseEntity<?> generar(@RequestBody GenerarValoresGuardiaDto dto) {
        try {
            valorGuardiaGeneracionService.generarTodosLosValores(dto.getFecha());
            return new ResponseEntity<>(new Mensaje("Valores de guardia generados correctamente"), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new Mensaje("Error al generar valores: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
