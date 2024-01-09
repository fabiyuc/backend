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

import com.guardias.backend.dto.DistribucionHorariaDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.DistribucionHoraria;
import com.guardias.backend.service.DistribucionHorariaService;

@RestController
@RequestMapping("/distribucionHoraria")
@CrossOrigin(origins = "http://localhost:4200")
public class DistribucionHorariaController {

    @Autowired
    DistribucionHorariaService distribucionHorariaService;

    @GetMapping("/lista")
    public ResponseEntity<List<DistribucionHoraria>> list() {
        List<DistribucionHoraria> list = distribucionHorariaService.list();
        return new ResponseEntity<List<DistribucionHoraria>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<DistribucionHoraria> getById(@PathVariable("id") Long id) {
        if (!distribucionHorariaService.existsById(id))
            return new ResponseEntity(new Mensaje("No existe la carga horaria"), HttpStatus.NOT_FOUND);
        DistribucionHoraria distribucionHoraria = distribucionHorariaService.findById(id).get();
        return new ResponseEntity<DistribucionHoraria>(distribucionHoraria, HttpStatus.OK);
    }

    @GetMapping("/detalleefector/{idEfector}")
    public ResponseEntity<List<DistribucionHoraria>> getByEfector(@PathVariable("idEfector") Long idEfector) {
        if (!distribucionHorariaService.existsByEfectorId(idEfector))
            return new ResponseEntity(new Mensaje("no existe la carga horaria"), HttpStatus.NOT_FOUND);
        List<DistribucionHoraria> distribucionHoraria = distribucionHorariaService.findByEfectorId(idEfector).get();
        return new ResponseEntity<>(distribucionHoraria, HttpStatus.OK);
    }

    @GetMapping("/detallepersona/{idPersona}")
    public ResponseEntity<List<DistribucionHoraria>> getByPersona(@PathVariable("idPersona") Long idPersona) {
        if (!distribucionHorariaService.existsByPersonaId(idPersona))
            return new ResponseEntity(new Mensaje("no existe la carga horaria"), HttpStatus.NOT_FOUND);
        List<DistribucionHoraria> distribucionHoraria = distribucionHorariaService.findByPersonaId(idPersona).get();
        return new ResponseEntity<>(distribucionHoraria, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody DistribucionHorariaDto distribucionHorariaDto) {

        if (distribucionHorariaDto.getCantidadHoras() == null)
            return new ResponseEntity(new Mensaje("la cantidad es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        DistribucionHoraria distribucionHoraria = new DistribucionHoraria(); // CLASE ABSTRACTA!!!!!
        distribucionHoraria.setFecha(distribucionHorariaDto.getFecha());
        distribucionHoraria.setHoraIngreso(distribucionHorariaDto.getHoraIngreso());
        distribucionHoraria.setPersona(distribucionHorariaDto.getPersona());
        distribucionHoraria.setEfector(distribucionHorariaDto.getEfector());
        distribucionHoraria.setCantidadHoras(distribucionHorariaDto.getCantidadHoras());

        distribucionHorariaService.save(distribucionHoraria);
        return new ResponseEntity(new Mensaje("Carga horaria creada"), HttpStatus.OK);
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id,
            @RequestBody DistribucionHorariaDto distribucionHorariaDto) {

        DistribucionHoraria distribucionHoraria = distribucionHorariaService.findById(id).get();

        return new ResponseEntity(new Mensaje("Carga horaria modificada"), HttpStatus.OK);
    }

}
