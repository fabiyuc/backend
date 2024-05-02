package com.guardias.backend.controller;

import java.math.BigDecimal;
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
import com.guardias.backend.dto.ValorGmiDto;
import com.guardias.backend.entity.ValorGmi;
import com.guardias.backend.service.ValorGmiService;

@RestController
@RequestMapping("/valorGmi")
@CrossOrigin(origins = "http://localhost:4200")
public class ValorGmiController {

    @Autowired
    ValorGmiService valorGmiService;

    @GetMapping("/list")
    public ResponseEntity<List<ValorGmi>> list() {
        List<ValorGmi> list = valorGmiService.findByActivoTrue();
        return new ResponseEntity<List<ValorGmi>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<ValorGmi>> listAll() {
        List<ValorGmi> list = valorGmiService.findAll();
        return new ResponseEntity<List<ValorGmi>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ValorGmi> getById(@PathVariable("id") Long id) {

        if (!valorGmiService.activo(id))
            return new ResponseEntity(new Mensaje("Valor no encontrado"), HttpStatus.NOT_FOUND);
        ValorGmi valorGmi = valorGmiService.findById(id).get();
        return new ResponseEntity(valorGmi, HttpStatus.OK);
    }

    private ResponseEntity<?> validations(ValorGmiDto valorGmiDto, Long id) {

        if (valorGmiDto.getFechaInicio() == null)
            return new ResponseEntity(new Mensaje("la fecha de inicio es obligatoria"), HttpStatus.BAD_REQUEST);

        if (valorGmiDto.getMonto().compareTo(BigDecimal.ZERO) < 0)
            return new ResponseEntity(new Mensaje("Monto incorrecto"), HttpStatus.BAD_REQUEST);

        if (valorGmiDto.getTipoGuardia() == null)
            return new ResponseEntity(new Mensaje("l tipo de guardia es obligatorio"), HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

}
