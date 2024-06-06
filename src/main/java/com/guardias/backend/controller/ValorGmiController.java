package com.guardias.backend.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.ValorGmiDto;
import com.guardias.backend.entity.Ddjj;
import com.guardias.backend.entity.ValorGmi;
import com.guardias.backend.service.DdjjService;
import com.guardias.backend.service.ValorGmiService;

@RestController
@RequestMapping("/valorGmi")
@CrossOrigin(origins = "http://localhost:4200")
public class ValorGmiController {

    @Autowired
    ValorGmiService valorGmiService;
    @Autowired
    DdjjService ddjjService;

    @GetMapping("/list")
    public ResponseEntity<List<ValorGmi>> list() {
        List<ValorGmi> list = valorGmiService.findByActivoTrue().get();
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

    // @GetMapping("/findByDate/{fecha}")
    // public ResponseEntity<List<ValorGmi>> findByDate(LocalDate fecha) {
    // List<ValorGmi> list = valorGmiService.findByDate(fecha);

    // if (list == null)
    // return new ResponseEntity(new Mensaje("Valor no encontrado"),
    // HttpStatus.NOT_FOUND);

    // return new ResponseEntity<List<ValorGmi>>(list, HttpStatus.OK);
    // }

    private ResponseEntity<?> validations(ValorGmiDto valorGmiDto) {

        if (valorGmiDto.getFechaInicio() == null)
            return new ResponseEntity(new Mensaje("la fecha de inicio es obligatoria"), HttpStatus.BAD_REQUEST);

        if (valorGmiDto.getMonto().compareTo(BigDecimal.ZERO) < 0)
            return new ResponseEntity(new Mensaje("Monto incorrecto"), HttpStatus.BAD_REQUEST);

        if (valorGmiDto.getTipoGuardia() == null)
            return new ResponseEntity(new Mensaje("El tipo de guardia es obligatorio"), HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    private ValorGmi createUpdate(ValorGmi valorGmi, ValorGmiDto valorGmiDto) {

        if (valorGmiDto.getFechaInicio() != null && !valorGmiDto.getFechaInicio().equals(valorGmi.getFechaInicio()))
            valorGmi.setFechaInicio(valorGmiDto.getFechaInicio());

        if (valorGmiDto.getFechaFin() != null && !valorGmiDto.getFechaFin().equals(valorGmi.getFechaFin()))
            valorGmi.setFechaFin(valorGmiDto.getFechaFin());

        if (valorGmiDto.getMonto() != null && !valorGmiDto.getMonto().equals(valorGmi.getMonto()))
            valorGmi.setMonto(valorGmiDto.getMonto());

        if (valorGmiDto.getTipoGuardia() != null && !valorGmiDto.getTipoGuardia().equals(valorGmi.getTipoGuardia()))
            valorGmi.setTipoGuardia(valorGmiDto.getTipoGuardia());

        if (valorGmiDto.getIdDdjjs() != null) {
            List<Long> idList = new ArrayList<Long>();
            if (valorGmi.getDdjjs() != null) {
                for (Ddjj ddjj : valorGmi.getDdjjs()) {
                    for (Long id : valorGmiDto.getIdDdjjs()) {
                        if (!ddjj.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            } else {
                valorGmi.setDdjjs(new ArrayList<>());
            }
            List<Long> idsToAdd = idList.isEmpty() ? valorGmiDto.getIdDdjjs() : idList;
            for (Long id : idsToAdd) {
                valorGmi.getDdjjs().add(ddjjService.findById(id).get());
                ddjjService.findById(id).get().setValorGmi(valorGmi);
            }
        }

        valorGmi.setActivo(true);
        return valorGmi;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ValorGmiDto valorGmiDto) {
        ResponseEntity<?> respuestaValidaciones = validations(valorGmiDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            ValorGmi valorGmi = createUpdate(new ValorGmi(), valorGmiDto);
            valorGmiService.save(valorGmi);
            return new ResponseEntity(new Mensaje("Valor creado correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody ValorGmiDto valorGmiDto) {

        if (!valorGmiService.activo(id))
            return new ResponseEntity(new Mensaje("El valor no existe"), HttpStatus.NOT_FOUND);
        ResponseEntity<?> respuestaValidaciones = validations(valorGmiDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            ValorGmi valorGmi = createUpdate(valorGmiService.findById(id).get(), valorGmiDto);
            valorGmiService.save(valorGmi);
            return new ResponseEntity(new Mensaje("Valor actualizado correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!valorGmiService.activo(id))
            return new ResponseEntity(new Mensaje("El valor no existe"), HttpStatus.NOT_FOUND);

        ValorGmi valorGmi = valorGmiService.findById(id).get();
        valorGmi.setActivo(false);
        valorGmiService.save(valorGmi);
        return new ResponseEntity(new Mensaje("Valor actualizado correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!valorGmiService.existsById(id))
            return new ResponseEntity(new Mensaje("El valor no existe"), HttpStatus.NOT_FOUND);

        valorGmiService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("Valor eliminado FISICAMENTEE"), HttpStatus.OK);
    }
}
