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

import com.guardias.backend.dto.DistribucionOtraDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.DistribucionHoraria;
import com.guardias.backend.entity.DistribucionOtra;
import com.guardias.backend.service.DistribucionOtraService;

@RestController
@RequestMapping("/distribucionOtra")
@CrossOrigin(origins = "http://localhost:4200")
public class DistribucionOtraController {

    @Autowired
    DistribucionOtraService distribucionOtraService;
    @Autowired
    DistribucionHorariaController distribucionHorariaController;

    @GetMapping("/list")
    public ResponseEntity<List<DistribucionOtra>> list() {
        List<DistribucionOtra> list = distribucionOtraService.findByActivoTrue().get();
        return new ResponseEntity<List<DistribucionOtra>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<DistribucionOtra>> listAll() {
        List<DistribucionOtra> list = distribucionOtraService.findAll();
        return new ResponseEntity<List<DistribucionOtra>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<DistribucionOtra> getById(@PathVariable("id") Long id) {
        if (!distribucionOtraService.activo(id))
            return new ResponseEntity(new Mensaje("No existe la carga horaria"), HttpStatus.NOT_FOUND);
        DistribucionOtra distribucionOtra = distribucionOtraService.findById(id).get();
        return new ResponseEntity<DistribucionOtra>(distribucionOtra, HttpStatus.OK);
    }

    @GetMapping("/list/{fechaInicio}")
    public ResponseEntity<List<DistribucionOtra>> getByFechainicio(
            @PathVariable("fechaInicio") LocalDate fechaInicio) {
        List<DistribucionOtra> list = distribucionOtraService.findByFechaInicio(fechaInicio);
        return new ResponseEntity<List<DistribucionOtra>>(list, HttpStatus.OK);
    }

    @GetMapping("/detailefector/{idEfector}")
    public ResponseEntity<List<DistribucionOtra>> getByEfector(@PathVariable("idEfector") Long idEfector) {
        if (!distribucionOtraService.existsByEfectorId(idEfector))
            return new ResponseEntity(new Mensaje("no existe la carga horaria"),
                    HttpStatus.NOT_FOUND);
        List<DistribucionOtra> distribucionOtra = distribucionOtraService.findByEfectorId(idEfector).get();
        return new ResponseEntity<>(distribucionOtra, HttpStatus.OK);
    }

    @GetMapping("/detailpersona/{idPersona}")
    public ResponseEntity<List<DistribucionOtra>> getByPersona(@PathVariable("idPersona") Long idPersona) {
        if (!distribucionOtraService.existsByPersonaId(idPersona))
            return new ResponseEntity(new Mensaje("no existe la carga horaria"),
                    HttpStatus.NOT_FOUND);
        List<DistribucionOtra> distribucionOtra = distribucionOtraService.findByPersonaId(idPersona).get();
        return new ResponseEntity<>(distribucionOtra, HttpStatus.OK);
    }

    DistribucionOtra createUpdate(DistribucionOtra distribucionOtra,
            DistribucionOtraDto distribucionOtraDto) {
        DistribucionHoraria distribucionHoraria = distribucionHorariaController.createUpdate(distribucionOtra,
                distribucionOtraDto);
        distribucionOtra = (DistribucionOtra) distribucionHoraria;

        if (distribucionOtraDto.getDescripcion() != (distribucionOtra.getDescripcion())
                && distribucionOtraDto.getDescripcion() != null)
            distribucionOtra.setDescripcion(distribucionOtraDto.getDescripcion());

        if (distribucionOtraDto.getLugar() != (distribucionOtra.getLugar())
                && distribucionOtraDto.getLugar() != null)
            distribucionOtra.setLugar(distribucionOtraDto.getLugar());

        distribucionOtra.setActivo(true);

        return distribucionOtra;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody DistribucionOtraDto distribucionOtraDto) {

        ResponseEntity<?> respuestaValidaciones = distribucionHorariaController.validations(distribucionOtraDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            DistribucionOtra distribucionOtra = createUpdate(new DistribucionOtra(),
                    distribucionOtraDto);
            distribucionOtraService.save(distribucionOtra);
            return new ResponseEntity(new Mensaje("Distribucion horaria creada"),
                    HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id,
            @RequestBody DistribucionOtraDto distribucionOtraDto) {

        if (!distribucionOtraService.activo(id))
            return new ResponseEntity(new Mensaje("La distribucion no existe"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = distribucionHorariaController.validations(distribucionOtraDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            DistribucionOtra distribucionOtra = createUpdate(
                    distribucionOtraService.findById(id).get(),
                    distribucionOtraDto);
            distribucionOtraService.save(distribucionOtra);
            return new ResponseEntity(new Mensaje("Distribucion horaria modificada correctamente"),
                    HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!distribucionOtraService.activo(id))
            return new ResponseEntity(new Mensaje("no existe la distribucion"), HttpStatus.NOT_FOUND);

        DistribucionOtra distribucionOtra = distribucionOtraService.findById(id).get();
        distribucionOtra.setActivo(false);
        distribucionOtraService.save(distribucionOtra);
        return new ResponseEntity(new Mensaje("distribucion eliminada correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") Long id) {

        if (!distribucionOtraService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la distribucion"), HttpStatus.NOT_FOUND);
        distribucionOtraService.deleteById(id);
        return new ResponseEntity(new Mensaje("distribucion eliminada FISICAMENTE"), HttpStatus.OK);
    }
}