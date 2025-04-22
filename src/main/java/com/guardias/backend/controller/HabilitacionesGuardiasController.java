package com.guardias.backend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

import com.guardias.backend.dto.AsistencialDto;
import com.guardias.backend.dto.HabilitacionesGuardiasDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.asistencial.AsistencialSummaryDto;
import com.guardias.backend.entity.HabilitacionesGuardia;
import com.guardias.backend.service.HabilitacionesGuardiasService;

@RestController
@RequestMapping("/habilitacionesGuardias")
@CrossOrigin(origins = "http://localhost:4200")
public class HabilitacionesGuardiasController {

    @Autowired
    HabilitacionesGuardiasService habilitacionesGuardiasService;

    // la lista de efectores debería actualizarse si hay bajas de efectores?
    @GetMapping("/list")
    public ResponseEntity<List<HabilitacionesGuardia>> list() {
        List<HabilitacionesGuardia> habilitacionesGuardiasList = habilitacionesGuardiasService.findByActivoTrue()
                .orElse(new ArrayList<>());

        return new ResponseEntity<List<HabilitacionesGuardia>>(habilitacionesGuardiasList, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<HabilitacionesGuardia>> listAll() {
        List<HabilitacionesGuardia> list = habilitacionesGuardiasService.findAll();
        return new ResponseEntity<List<HabilitacionesGuardia>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAsistencialesByEfector/{idEfector}")
    public ResponseEntity<List<HabilitacionesGuardia>> listHabilitacionesGuardiasByEfectorAndAsistencial(
            @PathVariable Long idEfector) {

        List<HabilitacionesGuardia> habilitacionesGuardias = habilitacionesGuardiasService
                .getHabilitacionesGuardiasByEfectorAndAsistencial(idEfector);
        return new ResponseEntity<>(habilitacionesGuardias, HttpStatus.OK);
    }

    // habilitaciones de guardias según el tipo de guardia EXTRA O CF
    @GetMapping("/listAsistencialesByEfectorAndTG/{idEfector}/{tipoGuardia}")
    public ResponseEntity<List<AsistencialSummaryDto>> getAsistencialesByEfectorAndTG(
            @PathVariable Long idEfector, @PathVariable String tipoGuardia) {

        List<AsistencialSummaryDto> habilitaciones = habilitacionesGuardiasService
                .getAsistencialesByEfectorAndTG(idEfector, tipoGuardia);

        if (habilitaciones.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok(habilitaciones);

    }

    @GetMapping("/listAsistencialesWithCfAndExtraByEfector/{idEfector}")
    public ResponseEntity<List<AsistencialDto>> getAsistencialesWithCfAndExtraByEfector(
            @PathVariable Long idEfector) {

        List<AsistencialDto> habilitaciones = habilitacionesGuardiasService
                .getAsistencialesWithCfAndExtraByEfector(idEfector);

        if (habilitaciones.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok(habilitaciones);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<HabilitacionesGuardia> getById(@PathVariable("id") Long id) {
        if (!habilitacionesGuardiasService.activo(id))
            return new ResponseEntity(new Mensaje("No existe la habilitacion de guardia con ese id"),
                    HttpStatus.NOT_FOUND);
        HabilitacionesGuardia habilitacionesGuardias = habilitacionesGuardiasService.findById(id).get();
        return new ResponseEntity<HabilitacionesGuardia>(habilitacionesGuardias, HttpStatus.OK);
    }

    @GetMapping("/detailAsistencial/{idAsistencial}")
    public ResponseEntity<?> getByAsistencial(@PathVariable("idAsistencial") Long idAsistencial) {
        // Obtén la habilitación activa
        Optional<HabilitacionesGuardia> habilitacionActiva = habilitacionesGuardiasService
                .findActivoByAsistencial(idAsistencial);

        // Si no hay habilitación activa, responde con un mensaje de error
        if (habilitacionActiva.isEmpty()) {
            return new ResponseEntity<>(new Mensaje("No existe una habilitación activa para este asistencial"),
                    HttpStatus.NOT_FOUND);
        }

        // Si hay habilitación activa, devuelve la habilitación
        return new ResponseEntity<>(habilitacionActiva.get(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody HabilitacionesGuardiasDto habilitacionesGuardiasDto) {

        ResponseEntity<?> respuestaValidaciones = habilitacionesGuardiasService.validations(habilitacionesGuardiasDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            HabilitacionesGuardia habilitacionesGuardias = habilitacionesGuardiasService
                    .createUpdate(new HabilitacionesGuardia(), habilitacionesGuardiasDto);
            habilitacionesGuardiasService.save(habilitacionesGuardias);
            return new ResponseEntity(new Mensaje("habilitaciones de Guardias creado"), HttpStatus.OK);
        }
        return respuestaValidaciones;
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id,
            @RequestBody HabilitacionesGuardiasDto habilitacionesGuardiasDto) {
        if (!habilitacionesGuardiasService.activo(id))
            return new ResponseEntity(new Mensaje("no existe la habilitaciones de Guardias"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = habilitacionesGuardiasService.validations(habilitacionesGuardiasDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            HabilitacionesGuardia habilitacionesGuardias = habilitacionesGuardiasService
                    .createUpdate(habilitacionesGuardiasService.findById(id).get(), habilitacionesGuardiasDto);
            habilitacionesGuardiasService.save(habilitacionesGuardias);
            return new ResponseEntity(new Mensaje("habilitaciones de Guardias modificado"), HttpStatus.OK);
        }
        return respuestaValidaciones;
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!habilitacionesGuardiasService.activo(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        HabilitacionesGuardia habilitacionesGuardias = habilitacionesGuardiasService.findById(id).get();
        habilitacionesGuardias.setActivo(false);
        habilitacionesGuardiasService.save(habilitacionesGuardias);
        return new ResponseEntity<>(new Mensaje("habilitaciones de Guardias eliminado correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!habilitacionesGuardiasService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        habilitacionesGuardiasService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("habilitaciones de Guardias eliminada FISICAMENTE"), HttpStatus.OK);
    }

    @GetMapping("/tieneHabilitacionesGuardias/{idAsistencial}/{idEfector}")
    public boolean tieneHabilitacionesGuardias(@PathVariable("idAsistencial") long idAsistencial,
            @PathVariable("idEfector") long idEfector) {
        return habilitacionesGuardiasService.tieneHabilitacionesGuardias(idAsistencial, idEfector);
    }

}
