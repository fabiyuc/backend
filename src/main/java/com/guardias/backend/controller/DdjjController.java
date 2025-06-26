package com.guardias.backend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.guardias.backend.dto.DdjjDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.ddjj.EstadoDdjjDto;
import com.guardias.backend.entity.Ddjj;
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.service.DdjjService;
import com.guardias.backend.service.ValorGmiService;

import jakarta.validation.ValidationException;

@Controller
@RequestMapping("/ddjj")
@CrossOrigin(origins = "http://localhost:4200")
public class DdjjController {
    @Autowired
    DdjjService ddjjService;

    @Autowired
    ValorGmiService valorGmiService;

    @GetMapping("/list")
    public ResponseEntity<List<Ddjj>> list() {
        List<Ddjj> list = ddjjService.findByActivoTrue();
        return new ResponseEntity<List<Ddjj>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Ddjj>> listAll() {
        List<Ddjj> list = ddjjService.findAll();
        return new ResponseEntity<List<Ddjj>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Ddjj> getById(@PathVariable("id") Long id) {
        if (!ddjjService.activo(id))
            return new ResponseEntity(new Mensaje("Valor no encontrado"), HttpStatus.NOT_FOUND);
        Ddjj ddjj = ddjjService.findById(id).get();
        return new ResponseEntity(ddjj, HttpStatus.OK);
    }

    @GetMapping("/listEfectorMes/{idEfector}/{mes}/{anio}")
    public ResponseEntity<List<Ddjj>> listEfectorMes(@PathVariable("idEfector") Long idEfector,
            @PathVariable("mes") String mes, @PathVariable("anio") int anio) {

        MesesEnum mesEnum = MesesEnum.valueOf(mes);

        if (!ddjjService.existsByAnioAndMes(anio, mesEnum))
            return new ResponseEntity(new Mensaje("La DDJJ no existe"), HttpStatus.NOT_FOUND);

        List<Ddjj> list = ddjjService.findByEfectorIdAndMesAndAnio(idEfector, mesEnum, anio);
        return new ResponseEntity<List<Ddjj>>(list, HttpStatus.OK);

    }

    @GetMapping("/listAnioMes/{mes}/{anio}")
    public ResponseEntity<List<Ddjj>> listAnioMes(@PathVariable("mes") String mes, @PathVariable("anio") int anio) {
        MesesEnum mesEnum = MesesEnum.valueOf(mes);

        if (!ddjjService.existsByAnioAndMes(anio, mesEnum))
            return new ResponseEntity(new Mensaje("La DDJJ no existe"), HttpStatus.NOT_FOUND);

        List<Ddjj> list = ddjjService.findByByAnioAndMes(anio, mesEnum);
        return new ResponseEntity<List<Ddjj>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAnio/{anio}")
    public ResponseEntity<List<Ddjj>> listAnio(@PathVariable("anio") int anio) {

        if (!ddjjService.existsByAnio(anio))
            return new ResponseEntity(new Mensaje("La DDJJ no existe"), HttpStatus.NOT_FOUND);

        List<Ddjj> list = ddjjService.findByByAnio(anio);
        return new ResponseEntity<List<Ddjj>>(list, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody DdjjDto ddjjDto) {
        ResponseEntity<?> respuestaValidaciones = ddjjService.validations(ddjjDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            Ddjj ddjj = ddjjService.createUpdate(new Ddjj(), ddjjDto);
            ddjjService.save(ddjj);
            return new ResponseEntity(new Mensaje("Declaracion Jurada creada correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody DdjjDto ddjjDto) {

        if (!ddjjService.activo(id))
            return new ResponseEntity(new Mensaje("DDJJ no existe"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = ddjjService.validations(ddjjDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            Ddjj ddjj = ddjjService.createUpdate(ddjjService.findById(id).get(), ddjjDto);
            ddjjService.save(ddjj);
            return new ResponseEntity(new Mensaje("Declaracion Jurada modificada correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!ddjjService.activo(id))
            return new ResponseEntity(new Mensaje("DDJJ no existe"), HttpStatus.NOT_FOUND);
        Ddjj ddjj = ddjjService.findById(id).get();
        ddjj.setActivo(false);
        ddjjService.save(ddjj);
        return new ResponseEntity(new Mensaje("Declaracion Jurada eliminada correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!ddjjService.existsById(id))
            return new ResponseEntity(new Mensaje("DDJJ no existe"), HttpStatus.NOT_FOUND);

        ddjjService.deleteById(id);
        return new ResponseEntity(new Mensaje("Declaracion Jurada eliminada FISICAMENTE"), HttpStatus.OK);
    }

    @PutMapping("/cambiarEstado")
    public ResponseEntity<?> cambiarEstado(@RequestBody EstadoDdjjDto estadoDdjjDto) {

        try {
            boolean resultado = ddjjService.cambiarEstado(estadoDdjjDto);
            if (resultado) {
                return new ResponseEntity<>(new Mensaje("Se cambió el estado de la Ddjj correctamente."),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new Mensaje("No se pudo cambiar el estado de la Ddjj."),
                        HttpStatus.BAD_REQUEST);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new Mensaje(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (ValidationException e) {
            return new ResponseEntity<>(new Mensaje(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

}
