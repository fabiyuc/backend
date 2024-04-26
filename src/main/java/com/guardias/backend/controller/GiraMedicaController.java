package com.guardias.backend.controller;

import java.time.LocalDate;
import java.util.List;

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

import com.guardias.backend.dto.GiraMedicaDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.GiraMedica;
import com.guardias.backend.service.GiraMedicaService;

import io.micrometer.common.util.StringUtils;

@Controller
@RequestMapping("/giraMedica")
@CrossOrigin(origins = "http://localhost:4200")
public class GiraMedicaController {

    @Autowired
    GiraMedicaService giraMedicaService;

    @GetMapping("/list")
    public ResponseEntity<List<GiraMedica>> list() {
        List<GiraMedica> list = giraMedicaService.findByActivo();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<GiraMedica>> listAll() {
        List<GiraMedica> list = giraMedicaService.findAll();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<GiraMedica>> getById(@PathVariable("id") Long id) {
        if (!giraMedicaService.existsById(id))
            return new ResponseEntity(new Mensaje("Gira medica  no encontrada"), HttpStatus.NOT_FOUND);
        GiraMedica giraMedica = giraMedicaService.findById(id).get();
        return new ResponseEntity(giraMedica, HttpStatus.OK);
    }

    @GetMapping("/detailfecha/{fecha}")
    public ResponseEntity<List<GiraMedica>> getByFecha(@PathVariable("fecha") LocalDate fecha) {
        if (!giraMedicaService.existsByFecha(fecha))
            return new ResponseEntity(new Mensaje("Gira medica  no encontrada"), HttpStatus.NOT_FOUND);
        List<GiraMedica> girasMedicas = giraMedicaService.findByFecha(fecha).get();
        return new ResponseEntity(girasMedicas, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody GiraMedicaDto giraMedicaDto) {
        if (giraMedicaDto.getFecha() == null)
            return new ResponseEntity(new Mensaje("La fecha es obligatoria"), HttpStatus.BAD_REQUEST);
        if (giraMedicaDto.getCantidadHoras() < 0)
            return new ResponseEntity(new Mensaje("Cantidad ded horas invalida"), HttpStatus.BAD_REQUEST);
        if (StringUtils.isBlank(giraMedicaDto.getDescripcion()))
            return new ResponseEntity(new Mensaje("La descripcion es obligatoria"), HttpStatus.BAD_REQUEST);

        GiraMedica giraMedica = new GiraMedica();
        giraMedica.setFecha(giraMedicaDto.getFecha());
        giraMedica.setCantidadHoras(giraMedicaDto.getCantidadHoras());
        giraMedica.setDescripcion(giraMedicaDto.getDescripcion());

        giraMedicaService.save(giraMedica);
        return new ResponseEntity(new Mensaje("Gira medica creada correctamente"), HttpStatus.OK);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody GiraMedicaDto giraMedicaDto) {
        if (giraMedicaDto.getFecha() == null)
            return new ResponseEntity(new Mensaje("La fecha es obligatoria"), HttpStatus.BAD_REQUEST);
        if (giraMedicaDto.getCantidadHoras() < 0)
            return new ResponseEntity(new Mensaje("Cantidad ded horas invalida"), HttpStatus.BAD_REQUEST);
        if (StringUtils.isBlank(giraMedicaDto.getDescripcion()))
            return new ResponseEntity(new Mensaje("La descripcion es obligatoria"), HttpStatus.BAD_REQUEST);

        GiraMedica giraMedica = giraMedicaService.findById(id).get();

        if (!giraMedicaDto.getFecha().equals(giraMedica.getFecha()))
            giraMedica.setFecha(giraMedicaDto.getFecha());
        if (giraMedicaDto.getCantidadHoras() != giraMedica.getCantidadHoras() && giraMedicaDto.getCantidadHoras() > 0)
            giraMedica.setCantidadHoras(giraMedicaDto.getCantidadHoras());
        if (!giraMedicaDto.getDescripcion().equals(giraMedica.getDescripcion()))
            giraMedica.setDescripcion(giraMedicaDto.getDescripcion());

        giraMedicaService.save(giraMedica);
        return new ResponseEntity(new Mensaje("Gira medica actualizada"), HttpStatus.OK);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!giraMedicaService.existsById(id))
            return new ResponseEntity(new Mensaje("Gira medica  no encontrada"), HttpStatus.NOT_FOUND);

        GiraMedica giraMedica = giraMedicaService.findById(id).get();
        giraMedica.setActivo(false);
        giraMedicaService.save(giraMedica);
        return new ResponseEntity(new Mensaje("gira medica eliminada"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") Long id) {
        if (!giraMedicaService.existsById(id))
            return new ResponseEntity(new Mensaje("Gira medica  no encontrada"), HttpStatus.NOT_FOUND);
        giraMedicaService.deleteById(id);
        return new ResponseEntity(new Mensaje("gira medica eliminada FISICAMENTE"), HttpStatus.OK);
    }

}
