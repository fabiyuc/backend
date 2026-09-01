package com.guardias.backend.controller;

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

import com.guardias.backend.dto.AsignacionHorasEfectorDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.asignacionHorasEfector.HorasDisponiblesEfectorDto;
import com.guardias.backend.dto.asignacionHorasEfector.ResumenAsignacionDto;
import com.guardias.backend.entity.AsignacionHorasEfector;
import com.guardias.backend.service.AsignacionHorasEfectorService;

@RestController
@RequestMapping("/asignacion-horas-efector")
@CrossOrigin(origins = "http://localhost:4200")
public class AsignacionHorasEfectorController {

    @Autowired
    AsignacionHorasEfectorService asignacionHorasEfectorService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody AsignacionHorasEfectorDto dto) {
        ResponseEntity<?> respuestaValidaciones = asignacionHorasEfectorService.validations(dto, 0L);
        if (respuestaValidaciones.getStatusCode() != HttpStatus.OK) {
            return respuestaValidaciones;
        }

        AsignacionHorasEfector asignacion = asignacionHorasEfectorService.createUpdate(
                new AsignacionHorasEfector(), dto);
        asignacionHorasEfectorService.save(asignacion);

        return new ResponseEntity<>(asignacion, HttpStatus.OK);
    }

    @GetMapping("/legajo/resumen/{idLegajo}/{anio}/{mes}")
    public ResponseEntity<?> resumenHoras(@PathVariable Long idLegajo,
            @PathVariable Integer anio,
            @PathVariable Integer mes) {
        try {
            ResumenAsignacionDto resumen = asignacionHorasEfectorService.resumenAsignacion(idLegajo, anio, mes);
            return new ResponseEntity<>(resumen, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new Mensaje(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/horas-disponibles-efector/{idLegajo}/{idEfector}/{anio}/{mes}")
    public ResponseEntity<?> horasDisponiblesEfector(@PathVariable Long idLegajo,
            @PathVariable Long idEfector,
            @PathVariable Integer anio,
            @PathVariable Integer mes) {
        try {
            HorasDisponiblesEfectorDto resultado = asignacionHorasEfectorService
                    .horasDisponiblesEfector(idLegajo, idEfector, anio, mes);
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new Mensaje(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/puede-modificar/{id}")
    public ResponseEntity<?> puedeModificar(@PathVariable Long id) {
        try {
            boolean resultado = asignacionHorasEfectorService.puedeModificar(id);
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new Mensaje(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody AsignacionHorasEfectorDto dto) {
        return asignacionHorasEfectorService.update(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return asignacionHorasEfectorService.delete(id);
    }

}
