package com.guardias.backend.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.NovedadPersonalDto;
import com.guardias.backend.entity.NovedadPersonal;
import com.guardias.backend.service.NovedadPersonalService;

@Controller
@RequestMapping("/novedadPersonal")
@CrossOrigin(origins = "http://localhost:4200")
public class NovedadPersonalController {

    @Autowired
    NovedadPersonalService novedadPersonalService;

    @GetMapping("/lista")
    public ResponseEntity<List<NovedadPersonal>> list() {
        List<NovedadPersonal> list = novedadPersonalService.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<NovedadPersonal> getById(@PathVariable("id") Long id) {
        if (!novedadPersonalService.existsById(id))
            return new ResponseEntity(new Mensaje("Novedad no encontrada"), HttpStatus.NOT_FOUND);
        NovedadPersonal novedadPersonal = novedadPersonalService.getById(id).get();
        return new ResponseEntity(novedadPersonal, HttpStatus.Status.OK);
    }

    @GetMapping("/detailpersona/{id}")
    public ResponseEntity<List<NovedadPersonal>> getByPersona(@PathVariable("id") Long id) {
        if (!novedadPersonalService.existsByPersona(id))
            return new ResponseEntity(new Mensaje("Novedad no encontrada"), HttpStatus.NOT_FOUND);
        List<NovedadPersonal> novedadesList = novedadPersonalService.getByPersona(id).get();
        return new ResponseEntity(novedadesList, HttpStatus.Status.OK);
    }

    @GetMapping("/detailfecha/{fecha}")
    public ResponseEntity<List<NovedadPersonal>> getByFecha(@PathVariable("fecha") LocalDate fecha) {
        if (!novedadPersonalService.existsByFecha(fecha))
            return new ResponseEntity(new Mensaje("Novedad no encontrada"), HttpStatus.NOT_FOUND);
        List<NovedadPersonal> novedadesList = novedadPersonalService.getByFecha(fecha).get();
        return new ResponseEntity(novedadesList, HttpStatus.Status.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody NovedadPersonalDto novedadPersonalDto) {
        if (novedadPersonalDto.getFechaInicio() == null)
            return new ResponseEntity(new Mensaje("la fecha es obligatoria"),
                    HttpStatus.BAD_REQUEST);
        if (novedadPersonalDto.getPersona() == null)
            return new ResponseEntity(new Mensaje("la persona es obligatoria"),
                    HttpStatus.BAD_REQUEST);
        if (novedadPersonalDto.getTipoLicencia() == null)
            return new ResponseEntity(new Mensaje("el tipo de licencia es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        NovedadPersonal novedadPersonal = new NovedadPersonal();

        novedadPersonal.setFechaInicio(novedadPersonalDto.getFechaInicio());
        novedadPersonal.setFechaFinal(novedadPersonalDto.getFechaFinal());
        novedadPersonal.setPuedeRealizarGuardia(novedadPersonalDto.isPuedeRealizarGuardia());
        novedadPersonal.setCobraSueldo(novedadPersonalDto.isCobraSueldo());
        novedadPersonal.setNecesitaReemplazo(novedadPersonalDto.isNecesitaReemplazo());
        novedadPersonal.setDescripcion(novedadPersonalDto.getDescripcion());
        novedadPersonal.setIdExtensionLicencia(novedadPersonalDto.getIdExtensionLicencia());
        novedadPersonal.setPersona(novedadPersonalDto.getPersona());
        novedadPersonal.setReemplazante(novedadPersonalDto.getReemplazante());
        novedadPersonal.setTipoLicencia(novedadPersonalDto.getTipoLicencia());

        novedadPersonalService.save(novedadPersonal);
        return new ResponseEntity(new Mensaje("Novedad creada correctamente"), HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody NovedadPersonalDto novedadPersonalDto) {
        if (!novedadPersonalService.existsByPersona(id))
            return new ResponseEntity(new Mensaje("Novedad no encontrada"), HttpStatus.NOT_FOUND);
        if (novedadPersonalDto.getFechaInicio() == null)
            return new ResponseEntity(new Mensaje("la fecha es obligatoria"),
                    HttpStatus.BAD_REQUEST);
        if (novedadPersonalDto.getPersona() == null)
            return new ResponseEntity(new Mensaje("la persona es obligatoria"),
                    HttpStatus.BAD_REQUEST);
        if (novedadPersonalDto.getTipoLicencia() == null)
            return new ResponseEntity(new Mensaje("el tipo de licencia es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        NovedadPersonal novedadPersonal = novedadPersonalService.getById(id).get();

        if (!novedadPersonalDto.getFechaInicio().isEqual(novedadPersonal.getFechaInicio()))
            novedadPersonal.setFechaInicio(novedadPersonalDto.getFechaInicio());

        if (!novedadPersonalDto.getFechaFinal().isEqual(novedadPersonal.getFechaFinal()))
            novedadPersonal.setFechaFinal(novedadPersonalDto.getFechaFinal());

        novedadPersonal.setPuedeRealizarGuardia(novedadPersonalDto.isPuedeRealizarGuardia());
        novedadPersonal.setCobraSueldo(novedadPersonalDto.isCobraSueldo());
        novedadPersonal.setNecesitaReemplazo(novedadPersonalDto.isNecesitaReemplazo());

        if (novedadPersonalDto.getDescripcion() != novedadPersonal.getDescripcion()
                && novedadPersonalDto.getDescripcion() != null && !novedadPersonalDto.getDescripcion().isEmpty())
            novedadPersonal.setDescripcion(novedadPersonalDto.getDescripcion());

        if (novedadPersonalDto.getIdExtensionLicencia() != novedadPersonal.getIdExtensionLicencia())
            novedadPersonal.setIdExtensionLicencia(novedadPersonalDto.getIdExtensionLicencia());

        if (novedadPersonalDto.getPersona() != novedadPersonal.getPersona() && novedadPersonalDto.getPersona() != null)
            novedadPersonal.setPersona(novedadPersonalDto.getPersona());

        if (novedadPersonalDto.getReemplazante() != novedadPersonal.getReemplazante()
                && novedadPersonalDto.getReemplazante() != null)
            novedadPersonal.setReemplazante(novedadPersonalDto.getReemplazante());

        if (novedadPersonalDto.getTipoLicencia() != novedadPersonal.getTipoLicencia()
                && novedadPersonalDto.getTipoLicencia() != null)
            novedadPersonal.setTipoLicencia(novedadPersonalDto.getTipoLicencia());

        novedadPersonalService.save(novedadPersonal);
        return new ResponseEntity(new Mensaje("Novedad creada correctamente"), HttpStatus.OK);
    }

}
