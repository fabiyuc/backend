package com.guardias.backend.controller;

import java.time.LocalDate;
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

import com.guardias.backend.dto.DistribucionGiraDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.DistribucionGira;
import com.guardias.backend.service.DistribucionGiraService;

@RestController
@RequestMapping("/distribucionGira")
@CrossOrigin(origins = "http://localhost:4200")
public class DistribucionGiraController {

    @Autowired
    DistribucionGiraService distribucionGiraService;

    @GetMapping("/list")
    public ResponseEntity<List<DistribucionGira>> list() {
        List<DistribucionGira> list = distribucionGiraService.findByActivo(true);
        return new ResponseEntity<List<DistribucionGira>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<DistribucionGira>> listAll() {
        List<DistribucionGira> list = distribucionGiraService.findAll();
        return new ResponseEntity<List<DistribucionGira>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<DistribucionGira> getById(@PathVariable("id") Long id) {
        if (!distribucionGiraService.existsById(id))
            return new ResponseEntity(new Mensaje("No existe la carga horaria"), HttpStatus.NOT_FOUND);
        DistribucionGira distribucionGira = distribucionGiraService.findById(id).get();
        return new ResponseEntity<DistribucionGira>(distribucionGira, HttpStatus.OK);
    }

    @GetMapping("/list/{fechaInicio}")
    public ResponseEntity<List<DistribucionGira>> getByFechainicio(
            @PathVariable("fechaInicio") LocalDate fechaInicio) {
        List<DistribucionGira> list = distribucionGiraService.findByFechaInicio(fechaInicio);
        return new ResponseEntity<List<DistribucionGira>>(list, HttpStatus.OK);
    }

    @GetMapping("/detailefector/{idEfector}")
    public ResponseEntity<List<DistribucionGira>> getByEfector(@PathVariable("idEfector") Long idEfector) {
        if (!distribucionGiraService.existsByEfectorId(idEfector))
            return new ResponseEntity(new Mensaje("no existe la carga horaria"),
                    HttpStatus.NOT_FOUND);
        List<DistribucionGira> distribucionGira = distribucionGiraService.findByEfectorId(idEfector).get();
        return new ResponseEntity<>(distribucionGira, HttpStatus.OK);
    }

    @GetMapping("/detailpersona/{idPersona}")
    public ResponseEntity<List<DistribucionGira>> getByPersona(@PathVariable("idPersona") Long idPersona) {
        if (!distribucionGiraService.existsByPersonaId(idPersona))
            return new ResponseEntity(new Mensaje("no existe la carga horaria"),
                    HttpStatus.NOT_FOUND);
        List<DistribucionGira> distribucionGira = distribucionGiraService.findByPersonaId(idPersona).get();
        return new ResponseEntity<>(distribucionGira, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody DistribucionGiraDto distribucionGiraDto) {

        if (distribucionGiraDto.getDia() == null)
            return new ResponseEntity(new Mensaje("El dia es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionGiraDto.getFechaInicio() == null)
            return new ResponseEntity(new Mensaje("la fecha de inicio es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionGiraDto.getHoraIngreso() == null)
            return new ResponseEntity(new Mensaje("la hora de ingreso es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionGiraDto.getCantidadHoras() == null)
            return new ResponseEntity(new Mensaje("la cantidad es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionGiraDto.getEfector() == null)
            return new ResponseEntity(new Mensaje("El efector es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionGiraDto.getPersona() == null)
            return new ResponseEntity(new Mensaje("la persona es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        DistribucionGira distribucionGira = new DistribucionGira();
        distribucionGira.setDia(distribucionGiraDto.getDia());
        distribucionGira.setFechaInicio(distribucionGiraDto.getFechaInicio());
        distribucionGira.setFechaFinalizacion(distribucionGiraDto.getFechaFinalizacion());
        distribucionGira.setHoraIngreso(distribucionGiraDto.getHoraIngreso());
        distribucionGira.setPersona(distribucionGiraDto.getPersona());
        distribucionGira.setEfector(distribucionGiraDto.getEfector());
        distribucionGira.setCantidadHoras(distribucionGiraDto.getCantidadHoras());

        distribucionGira.setDestino(distribucionGiraDto.getDestino());
        distribucionGira.setDescripcion(distribucionGiraDto.getDescripcion());

        distribucionGiraService.save(distribucionGira);
        return new ResponseEntity(new Mensaje("Carga horaria creada"),
                HttpStatus.OK);
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id,

            @RequestBody DistribucionGiraDto distribucionGiraDto) {

        if (!distribucionGiraService.existsById(id))
            return new ResponseEntity(new Mensaje("La distribucion no existe"), HttpStatus.NOT_FOUND);

        if (distribucionGiraDto.getDia() == null)
            return new ResponseEntity(new Mensaje("El dia es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionGiraDto.getFechaInicio() == null)
            return new ResponseEntity(new Mensaje("la fecha de inicio es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionGiraDto.getHoraIngreso() == null)
            return new ResponseEntity(new Mensaje("la hora de ingreso es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionGiraDto.getCantidadHoras() == null)
            return new ResponseEntity(new Mensaje("la cantidad es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionGiraDto.getEfector() == null)
            return new ResponseEntity(new Mensaje("El efector es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionGiraDto.getPersona() == null)
            return new ResponseEntity(new Mensaje("la persona es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        DistribucionGira distribucionGira = distribucionGiraService.findById(id).get();

        if (!distribucionGiraDto.getDia().equals(distribucionGira.getDia()))
            distribucionGira.setDia(distribucionGiraDto.getDia());
        if (!distribucionGiraDto.getFechaInicio().equals(distribucionGira.getFechaInicio()))
            distribucionGira.setFechaInicio(distribucionGiraDto.getFechaInicio());
        if (!distribucionGiraDto.getFechaFinalizacion().equals(distribucionGira.getFechaFinalizacion()))
            distribucionGira.setFechaFinalizacion(distribucionGiraDto.getFechaFinalizacion());
        if (!distribucionGiraDto.getHoraIngreso().equals(distribucionGira.getHoraIngreso()))
            distribucionGira.setHoraIngreso(distribucionGiraDto.getHoraIngreso());
        if (!distribucionGiraDto.getPersona().equals(distribucionGira.getPersona()))
            distribucionGira.setPersona(distribucionGiraDto.getPersona());
        if (!distribucionGiraDto.getEfector().equals(distribucionGira.getEfector()))
            distribucionGira.setEfector(distribucionGiraDto.getEfector());
        if (!distribucionGiraDto.getCantidadHoras().equals(distribucionGira.getCantidadHoras()))
            distribucionGira.setCantidadHoras(distribucionGiraDto.getCantidadHoras());

        if (!distribucionGiraDto.getDestino().equals(distribucionGira.getDestino()))
            distribucionGira.setDestino(distribucionGiraDto.getDestino());
        if (!distribucionGiraDto.getDescripcion().equals(distribucionGira.getDescripcion()))
            distribucionGira.setDescripcion(distribucionGiraDto.getDescripcion());

        distribucionGiraService.save(distribucionGira);

        return new ResponseEntity(new Mensaje("Carga horaria modificada"),
                HttpStatus.OK);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!distribucionGiraService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la distribucion"), HttpStatus.NOT_FOUND);

        DistribucionGira distribucionGira = distribucionGiraService.findById(id).get();
        distribucionGira.setActivo(false);
        distribucionGiraService.save(distribucionGira);
        return new ResponseEntity(new Mensaje("distribucion eliminada correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") Long id) {

        if (!distribucionGiraService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la distribucion"), HttpStatus.NOT_FOUND);
        distribucionGiraService.deleteById(id);
        return new ResponseEntity(new Mensaje("distribucion eliminada FISICAMENTE"), HttpStatus.OK);
    }
}