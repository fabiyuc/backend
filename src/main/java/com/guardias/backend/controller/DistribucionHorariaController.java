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

    @GetMapping("/detail/{cantidad}")
    public ResponseEntity<DistribucionHoraria> getByCantidad(@PathVariable("cantidad") int cantidad) {
        if (!distribucionHorariaService.existsByCantidad(cantidad))
            return new ResponseEntity(new Mensaje("no existe la carga horaria"), HttpStatus.NOT_FOUND);
        DistribucionHoraria distribucionHoraria = distribucionHorariaService.getByCantidad(cantidad).get();
        return new ResponseEntity<DistribucionHoraria>(distribucionHoraria, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody DistribucionHorariaDto distribucionHorariaDto) {

        if (distribucionHorariaDto.getCantidad() < 1)
            return new ResponseEntity(new Mensaje("la cantidad es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        DistribucionHoraria distribucionHoraria = new DistribucionHoraria();
        distribucionHoraria.setCantidad(distribucionHorariaDto.getCantidad());
        distribucionHoraria.setDescripcion(distribucionHorariaDto.getDescripcion());
        distribucionHorariaService.save(distribucionHoraria);
        return new ResponseEntity(new Mensaje("Carga horaria creada"), HttpStatus.OK);
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id,
            @RequestBody DistribucionHorariaDto distribucionHorariaDto) {

        if (!distribucionHorariaService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la distribucion horaria"), HttpStatus.NOT_FOUND);

        if (distribucionHorariaDto.getCantidad() < 1)
            return new ResponseEntity(new Mensaje("la cantidad es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        DistribucionHoraria distribucionHoraria = distribucionHorariaService.findById(id).get();

        if (distribucionHoraria.getCantidad() != distribucionHorariaDto.getCantidad()
                && distribucionHorariaDto.getCantidad() > 0)
            distribucionHoraria.setCantidad(distribucionHorariaDto.getCantidad());
        if (!distribucionHoraria.getDescripcion().equals(distribucionHorariaDto.getDescripcion()))
            distribucionHoraria.setDescripcion(distribucionHorariaDto.getDescripcion());
        distribucionHorariaService.save(distribucionHoraria);
        return new ResponseEntity(new Mensaje("Carga horaria modificada"), HttpStatus.OK);
    }

}
