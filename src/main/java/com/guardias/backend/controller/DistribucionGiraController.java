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

import com.guardias.backend.dto.DistribucionGiraDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.DistribucionGira;
import com.guardias.backend.service.DistribucionGiraService;

@RestController
@RequestMapping("/distribucionGira")
@CrossOrigin(origins = "http://localhost:4200")
public class DistribucionGiraController {

    @Autowired
    DistribucionGiraService distribucionGiraService;

    @GetMapping("/lista")
    public ResponseEntity<List<DistribucionGira>> list() {
        List<DistribucionGira> list = distribucionGiraService.list();
        return new ResponseEntity<List<DistribucionGira>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<DistribucionGira> getById(@PathVariable("id") Long id) {
        if (!distribucionGiraService.existsById(id))
            return new ResponseEntity(new Mensaje("No existe la carga horaria"), HttpStatus.NOT_FOUND);
        DistribucionGira distribucionGira = distribucionGiraService.findById(id).get();
        return new ResponseEntity<DistribucionGira>(distribucionGira, HttpStatus.OK);
    }

    @GetMapping("/detalleefector/{idEfector}")
    public ResponseEntity<List<DistribucionGira>> getByEfector(@PathVariable("idEfector") Long idEfector) {
        if (!distribucionGiraService.existsByEfectorId(idEfector))
            return new ResponseEntity(new Mensaje("no existe la carga horaria"),
                    HttpStatus.NOT_FOUND);
        List<DistribucionGira> distribucionGira = distribucionGiraService.findByEfectorId(idEfector).get();
        return new ResponseEntity<>(distribucionGira, HttpStatus.OK);
    }

    @GetMapping("/detallepersona/{idPersona}")
    public ResponseEntity<List<DistribucionGira>> getByPersona(@PathVariable("idPersona") Long idPersona) {
        if (!distribucionGiraService.existsByPersonaId(idPersona))
            return new ResponseEntity(new Mensaje("no existe la carga horaria"),
                    HttpStatus.NOT_FOUND);
        List<DistribucionGira> distribucionGira = distribucionGiraService.findByPersonaId(idPersona).get();
        return new ResponseEntity<>(distribucionGira, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody DistribucionGiraDto distribucionGiraDto) {

        if (distribucionGiraDto.getCantidadHoras() == null)
            return new ResponseEntity(new Mensaje("la cantidad es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        DistribucionGira distribucionGira = new DistribucionGira(); // CLASE ABSTRACTA!!!!!
        distribucionGira.setFecha(distribucionGiraDto.getFecha());
        distribucionGira.setHoraIngreso(distribucionGiraDto.getHoraIngreso());
        distribucionGira.setPersona(distribucionGiraDto.getPersona());
        distribucionGira.setEfector(distribucionGiraDto.getEfector());
        distribucionGira.setCantidadHoras(distribucionGiraDto.getCantidadHoras());

        distribucionGiraService.save(distribucionGira);
        return new ResponseEntity(new Mensaje("Carga horaria creada"),
                HttpStatus.OK);
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id,

            @RequestBody DistribucionGiraDto distribucionGiraDto) {

        DistribucionGira distribucionGira = distribucionGiraService.findById(id).get();

        return new ResponseEntity(new Mensaje("Carga horaria modificada"),
                HttpStatus.OK);
    }

}