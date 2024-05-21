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
import com.guardias.backend.entity.PendientesSemanal;
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.service.PendientesSemanalService;

@RestController
@RequestMapping("/pendientesSemanal")
@CrossOrigin(origins = "http://localhost:4200")
public class PendientesSemanalController {
    @Autowired
    PendientesSemanalService pendientesSemanalService;

    @GetMapping("/list")
    public ResponseEntity<List<PendientesSemanal>> list() {
        List<PendientesSemanal> list = pendientesSemanalService.findByActivo();
        return new ResponseEntity<List<PendientesSemanal>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<PendientesSemanal>> listAll() {
        List<PendientesSemanal> list = pendientesSemanalService.findAll();
        return new ResponseEntity<List<PendientesSemanal>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<PendientesSemanal> getById(@PathVariable("id") Long id) {
        if (!pendientesSemanalService.activo(id))
            return new ResponseEntity(new Mensaje("No se encontraron pendientes semanales"), HttpStatus.NOT_FOUND);
        PendientesSemanal pendientesSemanal = pendientesSemanalService.findById(id).get();
        return new ResponseEntity(pendientesSemanal, HttpStatus.OK);
    }

    @GetMapping("/listMes/{idEfector}/{mes}/{anio}")
    public ResponseEntity<List<PendientesSemanal>> getByMes(@PathVariable("idAsistencial") Long idAsistencial,
            @PathVariable("idEfector") Long idEfector,
            @PathVariable("mes") String mes, @PathVariable("anio") int anio) {

        MesesEnum mesEnum = MesesEnum.valueOf(mes);

        try {
            List<PendientesSemanal> list = pendientesSemanalService.findByAnioAndMesAndEfectorId(anio, mesEnum,
                    idEfector);
            return new ResponseEntity<List<PendientesSemanal>>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Mensaje("Registro no encontrado"), HttpStatus.BAD_REQUEST);
        }
    }
}
