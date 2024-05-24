package com.guardias.backend.controller;

import java.time.LocalDate;
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
import com.guardias.backend.entity.RegistrosPendientes;
import com.guardias.backend.service.EfectorService;
import com.guardias.backend.service.RegistroActividadService;
import com.guardias.backend.service.RegistrosPendientesService;

@RestController
@RequestMapping("/registrosPendientes")
@CrossOrigin(origins = "http://localhost:4200")
public class RegistrosPendientesController {

    @Autowired
    RegistrosPendientesService registrosPendientesService;
    @Autowired
    RegistroMensualController registroMensualController;
    @Autowired
    EfectorService efectorService;
    @Autowired
    RegistroActividadService registroActividadService;

    @GetMapping("/list")
    public ResponseEntity<List<RegistrosPendientes>> list() {
        List<RegistrosPendientes> list = registrosPendientesService.findByActivo();
        return new ResponseEntity<List<RegistrosPendientes>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<RegistrosPendientes>> listAll() {
        List<RegistrosPendientes> list = registrosPendientesService.findAll();
        return new ResponseEntity<List<RegistrosPendientes>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<RegistrosPendientes> getById(@PathVariable("id") Long id) {
        if (!registrosPendientesService.activo(id))
            return new ResponseEntity(new Mensaje("No se encontraron registros pendientes"), HttpStatus.NOT_FOUND);
        RegistrosPendientes registrosPendientes = registrosPendientesService.findById(id).get();
        return new ResponseEntity(registrosPendientes, HttpStatus.OK);
    }

    // List<RegistrosPendientes> findByEfector(Long idEfector)
    @GetMapping("/detailByEfector/{idEfector}")
    public ResponseEntity<List<RegistrosPendientes>> getByEfector(Long idEfector) {
        List<RegistrosPendientes> list = registrosPendientesService.findByEfectorId(efectorService.findById(idEfector));

        if (list != null)
            return new ResponseEntity<List<RegistrosPendientes>>(list, HttpStatus.OK);
        else
            return new ResponseEntity(new Mensaje("No se encontraron registros pendientes"), HttpStatus.NOT_FOUND);
    }

    @GetMapping("/detailByEfectorAndFecha/{idEfector}/{fecha}")
    public ResponseEntity<RegistrosPendientes> getByEfectorAndFecha(Long idEfector, LocalDate fecha) {
        RegistrosPendientes registrosPendientes = registrosPendientesService
                .findByEfectorAndFecha(efectorService.findById(idEfector), fecha)
                .get();

        if (registrosPendientes != null)
            return new ResponseEntity<RegistrosPendientes>(registrosPendientes,
                    HttpStatus.OK);
        else
            return new ResponseEntity(new Mensaje("No se encontraron registros pendientes"), HttpStatus.NOT_FOUND);
    }

}
