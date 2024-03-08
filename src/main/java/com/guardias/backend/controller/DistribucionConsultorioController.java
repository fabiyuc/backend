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
import com.guardias.backend.dto.DistribucionConsultorioDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.DistribucionConsultorio;
import com.guardias.backend.entity.DistribucionHoraria;
import com.guardias.backend.service.DistribucionConsultorioService;

@RestController
@RequestMapping("/distribucionConsultorio")
@CrossOrigin(origins = "http://localhost:4200")
public class DistribucionConsultorioController {

    @Autowired
    DistribucionHorariaController distribucionHorariaController;

    @Autowired
    DistribucionConsultorioService distribucionConsultorioService;

    @GetMapping("/list")
    public ResponseEntity<List<DistribucionConsultorio>> list() {
        List<DistribucionConsultorio> list = distribucionConsultorioService.findByActivo(true);
        return new ResponseEntity<List<DistribucionConsultorio>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<DistribucionConsultorio>> listAll() {
        List<DistribucionConsultorio> list = distribucionConsultorioService.findAll();
        return new ResponseEntity<List<DistribucionConsultorio>>(list, HttpStatus.OK);
    }

    @GetMapping("/list/{fechaInicio}")
    public ResponseEntity<List<DistribucionConsultorio>> getByFechainicio(
            @PathVariable("fechaInicio") LocalDate fechaInicio) {
        List<DistribucionConsultorio> list = distribucionConsultorioService.findByFechaInicio(fechaInicio);
        return new ResponseEntity<List<DistribucionConsultorio>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<DistribucionConsultorio> getById(@PathVariable("id") Long id) {
        if (!distribucionConsultorioService.existsById(id))
            return new ResponseEntity(new Mensaje("No existe la carga horaria"), HttpStatus.NOT_FOUND);
        DistribucionConsultorio distribucionConsultorio = distribucionConsultorioService.findById(id).get();
        return new ResponseEntity<DistribucionConsultorio>(distribucionConsultorio, HttpStatus.OK);
    }

    @GetMapping("/detailefector/{idEfector}")
    public ResponseEntity<List<DistribucionConsultorio>> getByEfector(@PathVariable("idEfector") Long idEfector) {
        if (!distribucionConsultorioService.existsByEfectorId(idEfector))
            return new ResponseEntity(new Mensaje("no existe la carga horaria"),
                    HttpStatus.NOT_FOUND);
        List<DistribucionConsultorio> distribucionConsultorio = distribucionConsultorioService
                .findByEfectorId(idEfector).get();
        return new ResponseEntity<>(distribucionConsultorio, HttpStatus.OK);
    }

    @GetMapping("/detailpersona/{idPersona}")
    public ResponseEntity<List<DistribucionConsultorio>> getByPersona(@PathVariable("idPersona") Long idPersona) {
        if (!distribucionConsultorioService.existsByPersonaId(idPersona))
            return new ResponseEntity(new Mensaje("no existe la carga horaria"),
                    HttpStatus.NOT_FOUND);
        List<DistribucionConsultorio> distribucionConsultorio = distribucionConsultorioService
                .findByPersonaId(idPersona).get();
        return new ResponseEntity<>(distribucionConsultorio, HttpStatus.OK);
    }

    DistribucionConsultorio createUpdate(DistribucionConsultorio distribucionConsultorio,
            DistribucionConsultorioDto distribucionConsultorioDto) {
        DistribucionHoraria distribucionHoraria = distribucionHorariaController.createUpdate(distribucionConsultorio,
                distribucionConsultorioDto);

        distribucionConsultorio = (DistribucionConsultorio) distribucionHoraria;

        if (!distribucionConsultorioDto.getLugar().equals(distribucionConsultorio.getLugar()))
            distribucionConsultorio.setLugar(distribucionConsultorioDto.getLugar());
        if (!distribucionConsultorioDto.getEspecialidad().equals(distribucionConsultorio.getEspecialidad()))
            distribucionConsultorio.setEspecialidad(distribucionConsultorioDto.getEspecialidad());
        if (distribucionConsultorioDto.getCantidadTurnos() != distribucionConsultorio.getCantidadTurnos())
            distribucionConsultorio.setCantidadTurnos(distribucionConsultorioDto.getCantidadTurnos());
        return distribucionConsultorio;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody DistribucionConsultorioDto distribucionConsultorioDto) {

        ResponseEntity<?> respuestaValidaciones = distribucionHorariaController.validations(distribucionConsultorioDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            DistribucionConsultorio distribucionConsultorio = createUpdate(new DistribucionConsultorio(),
                    distribucionConsultorioDto);
            distribucionConsultorioService.save(distribucionConsultorio);
            return new ResponseEntity(new Mensaje("Distribucion horaria creada"),
                    HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id,
            @RequestBody DistribucionConsultorioDto distribucionConsultorioDto) {

        if (!distribucionConsultorioService.existsById(id))
            return new ResponseEntity(new Mensaje("La distribucion no existe"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = distribucionHorariaController.validations(distribucionConsultorioDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            DistribucionConsultorio distribucionConsultorio = createUpdate(
                    distribucionConsultorioService.findById(id).get(),
                    distribucionConsultorioDto);
            distribucionConsultorioService.save(distribucionConsultorio);
            return new ResponseEntity(new Mensaje("Distribucion horaria modificada correctamente"),
                    HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!distribucionConsultorioService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la distribucion"), HttpStatus.NOT_FOUND);

        DistribucionConsultorio distribucionConsultorio = distribucionConsultorioService.findById(id).get();
        distribucionConsultorio.setActivo(false);
        distribucionConsultorioService.save(distribucionConsultorio);
        return new ResponseEntity(new Mensaje("distribucion eliminada correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") Long id) {

        if (!distribucionConsultorioService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la distribucion"), HttpStatus.NOT_FOUND);
        distribucionConsultorioService.deleteById(id);
        return new ResponseEntity(new Mensaje("distribucion eliminada FISICAMENTE"), HttpStatus.OK);
    }

}