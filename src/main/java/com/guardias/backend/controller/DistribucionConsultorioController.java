package com.guardias.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guardias.backend.dto.DistribucionConsultorioDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.DistribucionConsultorio;
import com.guardias.backend.service.DistribucionConsultorioService;

@RestController
@RequestMapping("/distribucionConsultorio")
@CrossOrigin(origins = "http://localhost:4200")
public class DistribucionConsultorioController {

    @Autowired
    DistribucionConsultorioService distribucionConsultorioService;

    @GetMapping("/lista")
    public ResponseEntity<List<DistribucionConsultorio>> list() {
        List<DistribucionConsultorio> list = distribucionConsultorioService.list();
        return new ResponseEntity<List<DistribucionConsultorio>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<DistribucionConsultorio> getById(@PathVariable("id") Long id) {
        if (!distribucionConsultorioService.existsById(id))
            return new ResponseEntity(new Mensaje("No existe la carga horaria"), HttpStatus.NOT_FOUND);
        DistribucionConsultorio distribucionConsultorio = distribucionConsultorioService.findById(id).get();
        return new ResponseEntity<DistribucionConsultorio>(distribucionConsultorio, HttpStatus.OK);
    }

    @GetMapping("/detalleefector/{idEfector}")
    public ResponseEntity<List<DistribucionConsultorio>> getByEfector(@PathVariable("idEfector") Long idEfector) {
        if (!distribucionConsultorioService.existsByEfectorId(idEfector))
            return new ResponseEntity(new Mensaje("no existe la carga horaria"),
                    HttpStatus.NOT_FOUND);
        List<DistribucionConsultorio> distribucionConsultorio = distribucionConsultorioService
                .findByEfectorId(idEfector).get();
        return new ResponseEntity<>(distribucionConsultorio, HttpStatus.OK);
    }

    @GetMapping("/detallepersona/{idPersona}")
    public ResponseEntity<List<DistribucionConsultorio>> getByPersona(@PathVariable("idPersona") Long idPersona) {
        if (!distribucionConsultorioService.existsByPersonaId(idPersona))
            return new ResponseEntity(new Mensaje("no existe la carga horaria"),
                    HttpStatus.NOT_FOUND);
        List<DistribucionConsultorio> distribucionConsultorio = distribucionConsultorioService
                .findByPersonaId(idPersona).get();
        return new ResponseEntity<>(distribucionConsultorio, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody DistribucionConsultorioDto distribucionConsultorioDto) {

        if (distribucionConsultorioDto.getCantidadHoras() == null)
            return new ResponseEntity(new Mensaje("la cantidad es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        DistribucionConsultorio distribucionConsultorio = new DistribucionConsultorio(); // CLASE ABSTRACTA!!!!!
        distribucionConsultorio.setFecha(distribucionConsultorioDto.getFecha());
        distribucionConsultorio.setHoraIngreso(distribucionConsultorioDto.getHoraIngreso());
        distribucionConsultorio.setPersona(distribucionConsultorioDto.getPersona());
        distribucionConsultorio.setEfector(distribucionConsultorioDto.getEfector());
        distribucionConsultorio.setCantidadHoras(distribucionConsultorioDto.getCantidadHoras());

        distribucionConsultorioService.save(distribucionConsultorio);
        return new ResponseEntity(new Mensaje("Carga horaria creada"),
                HttpStatus.OK);
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id,

            @RequestBody DistribucionConsultorioDto distribucionConsultorioDto) {

        DistribucionConsultorio distribucionConsultorio = distribucionConsultorioService.findById(id).get();

        return new ResponseEntity(new Mensaje("Carga horaria modificada"),
                HttpStatus.OK);
    }

}