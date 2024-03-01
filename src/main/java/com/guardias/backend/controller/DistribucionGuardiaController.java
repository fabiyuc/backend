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

import com.guardias.backend.dto.DistribucionGuardiaDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.DistribucionGuardia;
import com.guardias.backend.entity.DistribucionHoraria;
import com.guardias.backend.service.DistribucionGuardiaService;

@RestController
@RequestMapping("/distribucionGuardia")
@CrossOrigin(origins = "http://localhost:4200")
public class DistribucionGuardiaController {

    @Autowired
    DistribucionGuardiaService distribucionGuardiaService;
    @Autowired
    DistribucionHorariaController distribucionHorariaController;

    @GetMapping("/list")
    public ResponseEntity<List<DistribucionGuardia>> list() {
        List<DistribucionGuardia> list = distribucionGuardiaService.list();
        return new ResponseEntity<List<DistribucionGuardia>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<DistribucionGuardia> getById(@PathVariable("id") Long id) {
        if (!distribucionGuardiaService.existsById(id))
            return new ResponseEntity(new Mensaje("No existe la carga horaria"), HttpStatus.NOT_FOUND);
        DistribucionGuardia distribucionGuardia = distribucionGuardiaService.findById(id).get();
        return new ResponseEntity<DistribucionGuardia>(distribucionGuardia, HttpStatus.OK);
    }

    @GetMapping("/list/{fechaInicio}")
    public ResponseEntity<List<DistribucionGuardia>> getByFechainicio(
            @PathVariable("fechaInicio") LocalDate fechaInicio) {
        List<DistribucionGuardia> list = distribucionGuardiaService.findByFechaInicio(fechaInicio);
        return new ResponseEntity<List<DistribucionGuardia>>(list, HttpStatus.OK);
    }

    @GetMapping("/detailefector/{idEfector}")
    public ResponseEntity<List<DistribucionGuardia>> getByEfector(@PathVariable("idEfector") Long idEfector) {
        if (!distribucionGuardiaService.existsByEfectorId(idEfector))
            return new ResponseEntity(new Mensaje("no existe la carga horaria"),
                    HttpStatus.NOT_FOUND);
        List<DistribucionGuardia> distribucionGuardia = distribucionGuardiaService.findByEfectorId(idEfector).get();
        return new ResponseEntity<>(distribucionGuardia, HttpStatus.OK);
    }

    @GetMapping("/detailpersona/{idPersona}")
    public ResponseEntity<List<DistribucionGuardia>> getByPersona(@PathVariable("idPersona") Long idPersona) {
        if (!distribucionGuardiaService.existsByPersonaId(idPersona))
            return new ResponseEntity(new Mensaje("no existe la carga horaria"),
                    HttpStatus.NOT_FOUND);
        List<DistribucionGuardia> distribucionGuardia = distribucionGuardiaService.findByPersonaId(idPersona).get();
        return new ResponseEntity<>(distribucionGuardia, HttpStatus.OK);
    }

    DistribucionGuardia createUpdate(DistribucionGuardia distribucionGuardia,
            DistribucionGuardiaDto distribucionGuardiaDto) {
        DistribucionHoraria distribucionHoraria = distribucionHorariaController.createUpdate(distribucionGuardia,
                distribucionGuardiaDto);
        distribucionGuardia = (DistribucionGuardia) distribucionHoraria;

        if (!distribucionGuardiaDto.getTipoGuardia().equals(distribucionGuardia.getTipoGuardia()))
            distribucionGuardia.setTipoGuardia(distribucionGuardiaDto.getTipoGuardia());

        return distribucionGuardia;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody DistribucionGuardiaDto distribucionGuardiaDto) {

        ResponseEntity<?> respuestaValidaciones = distribucionHorariaController.validations(distribucionGuardiaDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            DistribucionGuardia distribucionGuardia = createUpdate(new DistribucionGuardia(),
                    distribucionGuardiaDto);
            distribucionGuardiaService.save(distribucionGuardia);
            return new ResponseEntity(new Mensaje("Distribucion horaria creada"),
                    HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id,
            @RequestBody DistribucionGuardiaDto distribucionGuardiaDto) {

        if (!distribucionGuardiaService.existsById(id))
            return new ResponseEntity(new Mensaje("La distribucion no existe"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = distribucionHorariaController.validations(distribucionGuardiaDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            DistribucionGuardia distribucionGuardia = createUpdate(
                    distribucionGuardiaService.findById(id).get(),
                    distribucionGuardiaDto);
            distribucionGuardiaService.save(distribucionGuardia);
            return new ResponseEntity(new Mensaje("Distribucion horaria modificada correctamente"),
                    HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!distribucionGuardiaService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la distribucion"), HttpStatus.NOT_FOUND);

        DistribucionGuardia distribucionGuardia = distribucionGuardiaService.findById(id).get();
        distribucionGuardia.setActivo(false);
        distribucionGuardiaService.save(distribucionGuardia);
        return new ResponseEntity(new Mensaje("distribucion eliminada correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") Long id) {

        if (!distribucionGuardiaService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la distribucion"), HttpStatus.NOT_FOUND);
        distribucionGuardiaService.deleteById(id);
        return new ResponseEntity(new Mensaje("distribucion eliminada FISICAMENTE"), HttpStatus.OK);
    }
}