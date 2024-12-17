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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.HabilitacionesGeneralesDto;
import com.guardias.backend.entity.Efector;
import com.guardias.backend.entity.HabilitacionesGenerales;
import com.guardias.backend.entity.Person;
import com.guardias.backend.entity.Region;
import com.guardias.backend.service.HabilitacionesGeneralesService;
import com.guardias.backend.service.PersonService;
import com.guardias.backend.service.RegionService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/habilitacionesGenerales")
@CrossOrigin(origins = "http://localhost:4200")
public class HabilitacionesGeneralesController {

    @Autowired
    HabilitacionesGeneralesService habilitacionesGeneralesService;
    @Autowired
    RegionService regionService;
    @Autowired
    PersonService personService;

    // la lista de efectores debería actualizarse si hay bajas de efectores?
    @GetMapping("/list")
    public ResponseEntity<List<HabilitacionesGenerales>> list() {
        List<HabilitacionesGenerales> habilitacionesGeneralesList = habilitacionesGeneralesService.findByActivoTrue()
                .orElse(new ArrayList<>());

        return new ResponseEntity<List<HabilitacionesGenerales>>(habilitacionesGeneralesList, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<HabilitacionesGenerales>> listAll() {
        List<HabilitacionesGenerales> list = habilitacionesGeneralesService.findAll();
        return new ResponseEntity<List<HabilitacionesGenerales>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAsistencialesByEfector/{idEfector}")
    public ResponseEntity<List<HabilitacionesGenerales>> listHabilitacionesGeneralesByEfectorAndAsistencial(
            @PathVariable Long idEfector) {
        List<HabilitacionesGenerales> habilitacionesGenerales = habilitacionesGeneralesService
                .getHabilitacionesGeneralesByEfectorAndAsistencial(idEfector);
        return new ResponseEntity<>(habilitacionesGenerales, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<HabilitacionesGenerales> getById(@PathVariable("id") Long id) {
        if (!habilitacionesGeneralesService.activo(id))
            return new ResponseEntity(new Mensaje("No existe la habilitacion general con ese id"),
                    HttpStatus.NOT_FOUND);
        HabilitacionesGenerales habilitacionesGeneral = habilitacionesGeneralesService.findById(id).get();
        return new ResponseEntity<HabilitacionesGenerales>(habilitacionesGeneral, HttpStatus.OK);
    }

    @GetMapping("/detailAsistencial/{idPersona}")
    public ResponseEntity<HabilitacionesGenerales> getByAsistencial(@PathVariable("idPersona") Long idPersona) {
        if (!habilitacionesGeneralesService.activoByPersona(idPersona))
            return new ResponseEntity(new Mensaje("no existe la habilitacion general de este asistencial"),
                    HttpStatus.NOT_FOUND);
        HabilitacionesGenerales habilitacionesGenerales = habilitacionesGeneralesService.findByPersona(idPersona).get();
        return new ResponseEntity<HabilitacionesGenerales>(habilitacionesGenerales, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody HabilitacionesGeneralesDto habilitacionesGeneralesDto) {

        ResponseEntity<?> respuestaValidaciones = habilitacionesGeneralesService
                .validations(habilitacionesGeneralesDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            HabilitacionesGenerales habilitacionesGenerales = habilitacionesGeneralesService
                    .createUpdate(new HabilitacionesGenerales(), habilitacionesGeneralesDto);
            habilitacionesGeneralesService.save(habilitacionesGenerales);
            return new ResponseEntity(new Mensaje("habilitaciones generales creado"), HttpStatus.OK);
        }
        return respuestaValidaciones;
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id,
            @RequestBody HabilitacionesGeneralesDto habilitacionesGeneralesDto) {
        if (!habilitacionesGeneralesService.activo(id))
            return new ResponseEntity(new Mensaje("no existe la habilitaciones generales"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = habilitacionesGeneralesService
                .validations(habilitacionesGeneralesDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            HabilitacionesGenerales habilitacionesGenerales = habilitacionesGeneralesService
                    .createUpdate(habilitacionesGeneralesService.findById(id).get(), habilitacionesGeneralesDto);
            habilitacionesGeneralesService.save(habilitacionesGenerales);
            return new ResponseEntity(new Mensaje("habilitaciones generales modificado"), HttpStatus.OK);
        }
        return respuestaValidaciones;
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!habilitacionesGeneralesService.activo(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        HabilitacionesGenerales habilitacionesGenerales = habilitacionesGeneralesService.findById(id).get();
        habilitacionesGenerales.setActivo(false);
        habilitacionesGeneralesService.save(habilitacionesGenerales);
        return new ResponseEntity<>(new Mensaje("habilitaciones generales eliminado correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!habilitacionesGeneralesService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        habilitacionesGeneralesService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("habilitaciones generales eliminada FISICAMENTE"), HttpStatus.OK);
    }

    @GetMapping("/tieneHabilitacionesGenerales/{idPersona}/{idEfector}")
    public boolean tieneHabilitacionesGenerales(@PathVariable("idPersona") long idPersona,
            @PathVariable("idEfector") long idEfector) {
        return habilitacionesGeneralesService.tieneHabilitacionesGenerales(idPersona, idEfector);
    }/*
      * 
      * @PutMapping("/updateOrCreateHabilitacionesByRegion/{idPersona}/{idRegion}")
      * public ResponseEntity<?> updateOrCreateByRegion(@PathVariable("idPersona")
      * Long idPersona,
      * 
      * @PathVariable("idRegion") Long idRegion) {
      * try {
      * habilitacionesGeneralesService.updateOrCreateHabilitacionesByRegion(
      * idPersona, idRegion);
      * return new ResponseEntity<>(new
      * Mensaje("Habilitaciones generales procesadas correctamente"),
      * HttpStatus.OK);
      * } catch (EntityNotFoundException e) {
      * return new ResponseEntity<>(new Mensaje(e.getMessage()),
      * HttpStatus.NOT_FOUND);
      * } catch (IllegalArgumentException e) {
      * return new ResponseEntity<>(new Mensaje(e.getMessage()),
      * HttpStatus.BAD_REQUEST);
      * }
      * }
      */

      @PostMapping("/crearActualizar/{idPersona}/{idRegion}")
      public ResponseEntity<Void> crearOActualizarHabilitacionesGenerales(@PathVariable Long idPersona,
              @PathVariable Long idRegion) {
        try {
            // Obtener la persona

            Person persona = personService.findById(idPersona);
            if (persona == null) {
                throw new EntityNotFoundException("No se encontró la persona con id: " + idPersona);
            }

            // Obtener la región
            Region region = regionService.findById(idRegion)
                    .orElseThrow(() -> new EntityNotFoundException("Región no encontrada"));

            // Obtener la lista de efectores de la región
            List<Efector> efectoresDeRegion = region.getEfectores();

            // Buscar si ya existe un registro activo de HabilitacionesGenerales para la
            // persona
            Optional<HabilitacionesGenerales> optionalHabilitacionExistente = habilitacionesGeneralesService.findByPersona(idPersona);

        if (optionalHabilitacionExistente.isPresent()) {
                // Si existe, actualizamos la lista de efectores sin duplicar
                HabilitacionesGenerales habilitacionExistente = optionalHabilitacionExistente.get();
                List<Efector> efectoresActuales = habilitacionExistente.getEfectores();

                efectoresActuales.clear();

                // Añadir solo los efectores nuevos que no estén ya en la lista
                for (Efector efectorNuevo : efectoresDeRegion) {
                    if (!efectoresActuales.contains(efectorNuevo)) {
                        efectoresActuales.add(efectorNuevo);
                    }
                }

                habilitacionExistente.setEfectores(efectoresActuales);
                habilitacionesGeneralesService.save(habilitacionExistente);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                // Si no existe, creamos un nuevo registro de HabilitacionesGenerales
                HabilitacionesGenerales habilitacion = new HabilitacionesGenerales();
                habilitacion.setActivo(true); // O cualquier valor por defecto
                habilitacion.setPersona(persona);
                habilitacion.setEfectores(efectoresDeRegion);

                habilitacionesGeneralesService.save(habilitacion);
                return new ResponseEntity<>(HttpStatus.CREATED);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
