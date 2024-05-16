package com.guardias.backend.controller;

import java.time.LocalDate;
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
        List<GiraMedica> list = giraMedicaService.findByActivoTrue().get();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<GiraMedica>> listAll() {
        List<GiraMedica> list = giraMedicaService.findAll();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<GiraMedica> getById(@PathVariable("id") Long id) {
        if (!giraMedicaService.activo(id))
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

    private ResponseEntity<?> validations(GiraMedicaDto giraMedicaDto) {

        if (giraMedicaDto.getFecha() == null)
            return new ResponseEntity(new Mensaje("La fecha es obligatoria"), HttpStatus.BAD_REQUEST);
        if (giraMedicaDto.getCantidadHoras() < 0)
            return new ResponseEntity(new Mensaje("Cantidad ded horas invalida"), HttpStatus.BAD_REQUEST);
        if (StringUtils.isBlank(giraMedicaDto.getDescripcion()))
            return new ResponseEntity(new Mensaje("La descripcion es obligatoria"), HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("Valido"), HttpStatus.OK);
    }

    private GiraMedica createUpdate(GiraMedica giraMedica, GiraMedicaDto giraMedicaDto) {
        if (giraMedicaDto.getFecha() != null
                && giraMedica.getFecha() != giraMedicaDto.getFecha())
            giraMedica.setFecha(giraMedicaDto.getFecha());

        if (!Objects.equals(giraMedica.getCantidadHoras(), giraMedicaDto.getCantidadHoras()))
            giraMedica.setCantidadHoras(giraMedicaDto.getCantidadHoras());

        if (giraMedicaDto.getDescripcion() != null
                && giraMedica.getDescripcion() != giraMedicaDto.getDescripcion()
                && !giraMedicaDto.getDescripcion().isEmpty())
            giraMedica.setDescripcion(giraMedicaDto.getDescripcion());

        giraMedica.setActivo(true);
        return giraMedica;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody GiraMedicaDto giraMedicaDto) {
        ResponseEntity<?> respuestaValidaciones = validations(giraMedicaDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            GiraMedica giraMedica = createUpdate(new GiraMedica(), giraMedicaDto);
            giraMedicaService.save(giraMedica);
            return new ResponseEntity(new Mensaje("Gira medica creada correctamente"), HttpStatus.OK);
        }
        return respuestaValidaciones;
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody GiraMedicaDto giraMedicaDto) {
        if (!giraMedicaService.activo(id))
            return new ResponseEntity(new Mensaje("Gira medica  no encontrada"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = validations(giraMedicaDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            GiraMedica giraMedica = createUpdate(giraMedicaService.findById(id).get(), giraMedicaDto);
            giraMedicaService.save(giraMedica);
            return new ResponseEntity<>(new Mensaje("Gira medica modificada correctamente"), HttpStatus.OK);
        }
        return respuestaValidaciones;
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!giraMedicaService.activo(id))
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
