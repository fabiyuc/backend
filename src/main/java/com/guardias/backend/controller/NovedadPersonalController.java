package com.guardias.backend.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.NovedadPersonalDto;
import com.guardias.backend.dto.novedadPersonal.ConsultaLicenciaCompensatorioDto;
import com.guardias.backend.entity.NovedadPersonal;
import com.guardias.backend.service.NovedadPersonalService;

@Controller
@RequestMapping("/novedadPersonal")
@CrossOrigin(origins = "http://localhost:4200")
public class NovedadPersonalController {

    @Autowired
    NovedadPersonalService novedadPersonalService;

    @GetMapping("/list")
    public ResponseEntity<List<NovedadPersonal>> list() {
        List<NovedadPersonal> list = novedadPersonalService.findByActivoTrue().get();
        return new ResponseEntity<List<NovedadPersonal>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<NovedadPersonal>> listAll() {
        List<NovedadPersonal> list = novedadPersonalService.findAll();
        return new ResponseEntity<List<NovedadPersonal>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<NovedadPersonal> getById(@PathVariable("id") Long id) {
        if (!novedadPersonalService.activo(id))
            return new ResponseEntity(new Mensaje("Novedad no encontrada"), HttpStatus.NOT_FOUND);
        NovedadPersonal novedadPersonal = novedadPersonalService.findById(id).get();
        return new ResponseEntity<NovedadPersonal>(novedadPersonal, HttpStatus.OK);
    }

    @GetMapping("/detailpersona/{id}")
    public ResponseEntity<List<NovedadPersonal>> getByPersona(@PathVariable("id") Long id) {
        if (!novedadPersonalService.activoByPersona(id))
            return new ResponseEntity(new Mensaje("Novedad no encontrada"),
                    HttpStatus.NOT_FOUND);
        List<NovedadPersonal> novedadesList = novedadPersonalService.findByPersona(id).get();
        return new ResponseEntity(novedadesList, HttpStatus.OK);
    }

    @GetMapping("/detailPersonaAndActivo/{id}/{mes}/{anio}")
    public ResponseEntity<List<NovedadPersonal>> getByActivePersonaAndDate(
            @PathVariable("id") Long id,
            @PathVariable("mes") int mes,
            @PathVariable("anio") int anio) {

        if (!novedadPersonalService.activoByPersona(id)) {
            return new ResponseEntity(new Mensaje("Novedad no encontrada"), HttpStatus.NOT_FOUND);
        }

        Optional<List<NovedadPersonal>> novedadesList = novedadPersonalService.findActiveByPersonaAndDate(id, mes,
                anio);
        if (novedadesList.isEmpty() || novedadesList.get().isEmpty()) {
            return new ResponseEntity(new Mensaje("No hay novedades para el mes y año indicados"),
                    HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(novedadesList.get(), HttpStatus.OK);
    }

    @GetMapping("/detailfecha/{fecha}")
    public ResponseEntity<List<NovedadPersonal>> getByFecha(@PathVariable("fecha") LocalDate fecha) {
        if (!novedadPersonalService.existsByFechaInicio(fecha))
            return new ResponseEntity(new Mensaje("Novedad no encontrada"),
                    HttpStatus.NOT_FOUND);
        List<NovedadPersonal> novedadesList = novedadPersonalService.findByFechaInicio(fecha).get();
        return new ResponseEntity(novedadesList, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody NovedadPersonalDto novedadPersonalDto) {

        ResponseEntity<?> respuestaValidaciones = novedadPersonalService.validations(novedadPersonalDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            NovedadPersonal novedadPersonal = novedadPersonalService.createUpdate(new NovedadPersonal(),
                    novedadPersonalDto);
            novedadPersonalService.save(novedadPersonal);
            return new ResponseEntity(new Mensaje("Novedad creada correctamente"), HttpStatus.OK);

        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody NovedadPersonalDto novedadPersonalDto) {
        if (!novedadPersonalService.existsById(id))
            return new ResponseEntity(new Mensaje("Novedad no encontrada"),
                    HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = novedadPersonalService.validations(novedadPersonalDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            NovedadPersonal novedadPersonal = novedadPersonalService.createUpdate(
                    novedadPersonalService.findById(id).get(),
                    novedadPersonalDto);
            novedadPersonalService.save(novedadPersonal);
            return new ResponseEntity(new Mensaje("Novedad modificada correctamente"), HttpStatus.OK);

        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!novedadPersonalService.activo(id))
            return new ResponseEntity(new Mensaje("La novedad no exixte"), HttpStatus.NOT_FOUND);

        NovedadPersonal novedadPersonal = novedadPersonalService.findById(id).get();
        novedadPersonal.setActivo(false);
        novedadPersonalService.save(novedadPersonal);
        return new ResponseEntity<>(new Mensaje("novedad eliminada correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!novedadPersonalService.existsById(id))
            return new ResponseEntity(new Mensaje("La novedad no exixte"), HttpStatus.NOT_FOUND);
        novedadPersonalService.deleteById(null);
        return new ResponseEntity<>(new Mensaje("novedad eliminada FISICAMENTE"), HttpStatus.OK);
    }

    @GetMapping("/puedeHacerGuardia/{idPersona}/{fechaConsulta}")
    public ResponseEntity<Boolean> puedeHacerGuardia(@PathVariable Long idPersona,
            @PathVariable LocalDate fechaConsulta) {
        boolean resultado = novedadPersonalService.puedeHacerGuardia(idPersona, fechaConsulta);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/tieneLicenciaLAO/{idPersona}/{fechaConsulta}")
    public ResponseEntity<Boolean> tieneLicenciaLAO(@PathVariable Long idPersona,
            @PathVariable LocalDate fechaConsulta) {
        boolean resultado = novedadPersonalService.tieneLicenciaLAO(idPersona, fechaConsulta);
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/tieneLicenciaCompensatorio")
    public ResponseEntity<Boolean> tieneLicenciaCompensatorio(
            @RequestBody ConsultaLicenciaCompensatorioDto consulta) {

        boolean resultado = novedadPersonalService.tieneLicenciaCompensatorio(consulta);
        return ResponseEntity.ok(resultado);
    }

}