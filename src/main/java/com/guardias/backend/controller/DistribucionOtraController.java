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

import com.guardias.backend.dto.DistribucionOtraDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.DistribucionOtra;
import com.guardias.backend.service.DistribucionOtraService;

@RestController
@RequestMapping("/distribucionOtra")
@CrossOrigin(origins = "http://localhost:4200")
public class DistribucionOtraController {

    @Autowired
    DistribucionOtraService distribucionOtraService;

    @GetMapping("/lista")
    public ResponseEntity<List<DistribucionOtra>> list() {
        List<DistribucionOtra> list = distribucionOtraService.list();
        return new ResponseEntity<List<DistribucionOtra>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<DistribucionOtra> getById(@PathVariable("id") Long id) {
        if (!distribucionOtraService.existsById(id))
            return new ResponseEntity(new Mensaje("No existe la carga horaria"), HttpStatus.NOT_FOUND);
        DistribucionOtra distribucionOtra = distribucionOtraService.findById(id).get();
        return new ResponseEntity<DistribucionOtra>(distribucionOtra, HttpStatus.OK);
    }

    @GetMapping("/detalleefector/{idEfector}")
    public ResponseEntity<List<DistribucionOtra>> getByEfector(@PathVariable("idEfector") Long idEfector) {
        if (!distribucionOtraService.existsByEfectorId(idEfector))
            return new ResponseEntity(new Mensaje("no existe la carga horaria"),
                    HttpStatus.NOT_FOUND);
        List<DistribucionOtra> distribucionOtra = distribucionOtraService.findByEfectorId(idEfector).get();
        return new ResponseEntity<>(distribucionOtra, HttpStatus.OK);
    }

    @GetMapping("/detallepersona/{idPersona}")
    public ResponseEntity<List<DistribucionOtra>> getByPersona(@PathVariable("idPersona") Long idPersona) {
        if (!distribucionOtraService.existsByPersonaId(idPersona))
            return new ResponseEntity(new Mensaje("no existe la carga horaria"),
                    HttpStatus.NOT_FOUND);
        List<DistribucionOtra> distribucionOtra = distribucionOtraService.findByPersonaId(idPersona).get();
        return new ResponseEntity<>(distribucionOtra, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody DistribucionOtraDto distribucionOtraDto) {

        if (distribucionOtraDto.getCantidadHoras() == null)
            return new ResponseEntity(new Mensaje("la cantidad es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        DistribucionOtra distribucionOtra = new DistribucionOtra(); // CLASE ABSTRACTA!!!!!
        distribucionOtra.setFecha(distribucionOtraDto.getFecha());
        distribucionOtra.setHoraIngreso(distribucionOtraDto.getHoraIngreso());
        distribucionOtra.setPersona(distribucionOtraDto.getPersona());
        distribucionOtra.setEfector(distribucionOtraDto.getEfector());
        distribucionOtra.setCantidadHoras(distribucionOtraDto.getCantidadHoras());

        distribucionOtraService.save(distribucionOtra);
        return new ResponseEntity(new Mensaje("Carga horaria creada"),
                HttpStatus.OK);
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id,

            @RequestBody DistribucionOtraDto distribucionOtraDto) {

        DistribucionOtra distribucionOtra = distribucionOtraService.findById(id).get();

        return new ResponseEntity(new Mensaje("Carga horaria modificada"),
                HttpStatus.OK);
    }

}