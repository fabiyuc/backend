package com.guardias.backend.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guardias.backend.dto.FacturaDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.ddjj.AutoridadImagenDto;
import com.guardias.backend.entity.Factura;
import com.guardias.backend.entity.Legajo;
import com.guardias.backend.enums.QuincenaEnum;
import com.guardias.backend.security.entity.Usuario;
import com.guardias.backend.service.FacturaService;

@RestController
@RequestMapping("/factura")
@CrossOrigin(origins = "http://localhost:4200")
public class FacturaController {
    
    @Autowired
    FacturaService facturaService;

     @GetMapping("/list")
    public ResponseEntity<List<Factura>> list() {
        List<Factura> facturasList = facturaService.findByActivoTrue()
                .orElse(new ArrayList<>());

        return new ResponseEntity<List<Factura>>(facturasList, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Factura>> listAll() {
        List<Factura> list = facturaService.findAll();
        return new ResponseEntity<List<Factura>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Factura> getById(@PathVariable("id") Long id) {
        if (!facturaService.activo(id))
            return new ResponseEntity(new Mensaje("No existe la factura con ese id"),
                    HttpStatus.NOT_FOUND);
        Factura factura = facturaService.findById(id).get();
        return new ResponseEntity<Factura>(factura, HttpStatus.OK);
    }

    @GetMapping("/detailAsistencial/{idAsistencial}")
    public ResponseEntity<Factura> getByAsistencial(@PathVariable("idAsistencial") Long idAsistencial) {
        if (!facturaService.activoByAsistencial(idAsistencial))
            return new ResponseEntity(new Mensaje("no existe la factura de este asistencial"),
                    HttpStatus.NOT_FOUND);
        Factura factura = facturaService.findByAsistencial(idAsistencial).get();
        return new ResponseEntity<Factura>(factura, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody FacturaDto facturaDto) {

        ResponseEntity<?> respuestaValidaciones = facturaService
                .validations(facturaDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            Factura factura = facturaService
                    .createUpdate(new Factura(), facturaDto);
            facturaService.save(factura);
            return new ResponseEntity(new Mensaje("Factura creada"), HttpStatus.OK);
        }
        return respuestaValidaciones;
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id,
            @RequestBody FacturaDto facturaDto) {
        if (!facturaService.activo(id))
            return new ResponseEntity(new Mensaje("no existe la factura"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = facturaService
                .validations(facturaDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            Factura factura = facturaService
                    .createUpdate(facturaService.findById(id).get(), facturaDto);
            facturaService.save(factura);
            return new ResponseEntity(new Mensaje("factura modificada"), HttpStatus.OK);
        }
        return respuestaValidaciones;
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!facturaService.activo(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        Factura factura = facturaService.findById(id).get();
        factura.setActivo(false);
        facturaService.save(factura);
        return new ResponseEntity<>(new Mensaje("factura  eliminada correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!facturaService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        facturaService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("factura eliminada FISICAMENTE"), HttpStatus.OK);
    }

    @GetMapping("/getMontoByQuincena/{idAsistencial}/{idEfector}/{quincena}")
    public ResponseEntity<?> getMontoByQuincena(
        @PathVariable("idAsistencial") Long idAsistencial,
        @PathVariable("idEfector") Long idEfector,
        @PathVariable("quincena") String quincena) {

        QuincenaEnum quincenaEnum = QuincenaEnum.valueOf(quincena.toUpperCase());

        try {
            BigDecimal monto = facturaService.getMontoByQuincena(idAsistencial, idEfector, quincenaEnum);
  
            return new ResponseEntity<>(monto, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new Mensaje("Error al obtener el monto " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
