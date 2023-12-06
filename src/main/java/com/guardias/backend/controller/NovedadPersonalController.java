package com.guardias.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.NovedadPersonalDto;
import com.guardias.backend.entity.NovedadPersonal;
import com.guardias.backend.service.NovedadPersonalService;

@RestController
@RequestMapping("/NovedadPersonal")
@CrossOrigin(origins = "http://localhost:4200")
public class NovedadPersonalController {

    @Autowired
    NovedadPersonalService novedadPersonalService;

    @GetMapping("/lista")
    public ResponseEntity<List<NovedadPersonal>> list() {
        List<NovedadPersonal> list = novedadPersonalService.list();
        return new ResponseEntity<List<NovedadPersonal>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<NovedadPersonal> getById(@PathVariable("id") Long id) {
        if (!novedadPersonalService.existsById(id))
            return new ResponseEntity(new Mensaje("No existe la novedad"), HttpStatus.NOT_FOUND);
        NovedadPersonal novedadPersonal = novedadPersonalService.getById(id).get();
        return new ResponseEntity<NovedadPersonal>(novedadPersonal, HttpStatus.OK);
    }

    @GetMapping("/persona/{id}")
    public ResponseEntity<List<NovedadPersonal>> getByPersona(@PathVariable("id") Long id) {
        if (!novedadPersonalService.existsByPersona(id))
            return new ResponseEntity(new Mensaje("No existe la novedad"), HttpStatus.NOT_FOUND);
        List<NovedadPersonal> list = novedadPersonalService.findByPersona(id);
        return new ResponseEntity<List<NovedadPersonal>>(list, HttpStatus.OK);
    }

    @PutMapping("/create/{id}")
    public ResponseEntity<?> create(@RequestBody NovedadPersonalDto novedadPersonalDto) {
        if (novedadPersonalDto.getFechaInicio() == null)
            return new ResponseEntity(new Mensaje("la fecha de inicio es obligatoria"),
                    HttpStatus.BAD_REQUEST);
        if (novedadPersonalDto.getFechaFinal() == null)
            return new ResponseEntity(new Mensaje("la fecha de final es obligatoria"),
                    HttpStatus.BAD_REQUEST);
        if (novedadPersonalDto.getPersona() == null)
            return new ResponseEntity(new Mensaje("la persona es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        NovedadPersonal novedadPersonal = new NovedadPersonal();
        novedadPersonal.setFechaInicio(novedadPersonalDto.getFechaInicio());
        novedadPersonal.setFechaFinal(novedadPersonalDto.getFechaFinal());
        novedadPersonal.setPuedeRealizarGuardia(novedadPersonalDto.isPuedeRealizarGuardia());
        novedadPersonal.setCobraSueldo(novedadPersonalDto.isCobraSueldo());
        novedadPersonal.setNecesitaReemplazo(novedadPersonalDto.isNecesitaReemplazo());
        novedadPersonal.setDescripcion(novedadPersonalDto.getDescripcion());
        novedadPersonal.setPersona(novedadPersonalDto.getPersona());
        novedadPersonal.setReemplazante(novedadPersonalDto.getReemplazante());
        novedadPersonal.setTipoLicencia(novedadPersonalDto.getTipoLicencia());

        novedadPersonalService.save(novedadPersonal);
        return new ResponseEntity(new Mensaje("Novedad creada correctamente"), HttpStatus.OK);

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody NovedadPersonalDto novedadPersonalDto) {
        if (!novedadPersonalService.existsById(id))
            return new ResponseEntity(new Mensaje("No existe la novedad"),
                    HttpStatus.BAD_REQUEST);
        if (novedadPersonalDto.getFechaInicio() == null)
            return new ResponseEntity(new Mensaje("la fecha de inicio es obligatoria"),
                    HttpStatus.BAD_REQUEST);
        if (novedadPersonalDto.getFechaFinal() == null)
            return new ResponseEntity(new Mensaje("la fecha de final es obligatoria"),
                    HttpStatus.BAD_REQUEST);
        if (novedadPersonalDto.getPersona() == null)
            return new ResponseEntity(new Mensaje("la persona es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        NovedadPersonal novedadPersonal = novedadPersonalService.getById(id).get();

        if (!novedadPersonalDto.getFechaInicio().equals(novedadPersonal.getFechaInicio()))
            novedadPersonal.setFechaInicio(novedadPersonalDto.getFechaInicio());

        if (!novedadPersonalDto.getFechaFinal().equals(novedadPersonal.getFechaFinal()))
            novedadPersonal.setFechaFinal(novedadPersonalDto.getFechaFinal());

        novedadPersonal.setPuedeRealizarGuardia(novedadPersonalDto.isPuedeRealizarGuardia());
        novedadPersonal.setCobraSueldo(novedadPersonalDto.isCobraSueldo());
        novedadPersonal.setNecesitaReemplazo(novedadPersonalDto.isNecesitaReemplazo());

        if (!novedadPersonalDto.getDescripcion().equals(novedadPersonal.getDescripcion()))
            novedadPersonal.setDescripcion(novedadPersonalDto.getDescripcion());

        if (!novedadPersonalDto.getPersona().equals(novedadPersonal.getPersona()))
            novedadPersonal.setPersona(novedadPersonalDto.getPersona());

        if (!novedadPersonalDto.getReemplazante().equals(novedadPersonal.getReemplazante()))
            novedadPersonal.setReemplazante(novedadPersonalDto.getReemplazante());

        if (!novedadPersonalDto.getTipoLicencia().equals(novedadPersonal.getTipoLicencia()))
            novedadPersonal.setTipoLicencia(novedadPersonalDto.getTipoLicencia());

        novedadPersonalService.save(novedadPersonal);
        return new ResponseEntity(new Mensaje("Novedad creada correctamente"), HttpStatus.OK);

    }

}
