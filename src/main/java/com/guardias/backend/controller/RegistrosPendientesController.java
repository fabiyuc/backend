package com.guardias.backend.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.RegistroActividad;
import com.guardias.backend.entity.RegistrosPendientes;
import com.guardias.backend.service.EfectorService;
import com.guardias.backend.service.RegistroActividadService;
import com.guardias.backend.service.RegistrosPendientesService;

@RestController
@RequestMapping("/registrosPendientes")
@CrossOrigin(origins = "http://localhost:4200")
public class RegistrosPendientesController {

    @Autowired
    RegistrosPendientesService registrosPendientesService;
    @Autowired
    RegistroMensualController registroMensualController;
    @Autowired
    EfectorService efectorService;
    @Autowired
    RegistroActividadService registroActividadService;

    @GetMapping("/list")
    public ResponseEntity<List<RegistrosPendientes>> list() {
        List<RegistrosPendientes> list = registrosPendientesService.findByActivo();
        return new ResponseEntity<List<RegistrosPendientes>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<RegistrosPendientes>> listAll() {
        List<RegistrosPendientes> list = registrosPendientesService.findAll();
        return new ResponseEntity<List<RegistrosPendientes>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<RegistrosPendientes> getById(@PathVariable("id") Long id) {
        if (!registrosPendientesService.activo(id))
            return new ResponseEntity(new Mensaje("No se encontraron registros pendientes"), HttpStatus.NOT_FOUND);
        RegistrosPendientes registrosPendientes = registrosPendientesService.findById(id).get();
        return new ResponseEntity(registrosPendientes, HttpStatus.OK);
    }

    // List<RegistrosPendientes> findByEfector(Long idEfector)
    @GetMapping("/detailByEfector/{idEfector}")
    public ResponseEntity<List<RegistrosPendientes>> getByEfector(Long idEfector) {
        List<RegistrosPendientes> list = registrosPendientesService.findByEfectorId(efectorService.findById(idEfector));

        if (list != null)
            return new ResponseEntity<List<RegistrosPendientes>>(list, HttpStatus.OK);
        else
            return new ResponseEntity(new Mensaje("No se encontraron registros pendientes"), HttpStatus.NOT_FOUND);
    }

    @GetMapping("/detailByEfectorAndFecha/{idEfector}/{fecha}")
    public ResponseEntity<RegistrosPendientes> getByEfectorAndFecha(Long idEfector, LocalDate fecha) {
        RegistrosPendientes registrosPendientes = registrosPendientesService
                .findByEfectorAndFecha(efectorService.findById(idEfector), fecha)
                .get();

        if (registrosPendientes != null)
            return new ResponseEntity<RegistrosPendientes>(registrosPendientes,
                    HttpStatus.OK);
        else
            return new ResponseEntity(new Mensaje("No se encontraron registros pendientes"), HttpStatus.NOT_FOUND);
    }

    public RegistroActividad addRegistroActividad(RegistroActividad registroActividad) {

        RegistrosPendientes registrosPendientes = new RegistrosPendientes();
        try {
            // Busca un registro pendiente existente basado en el efector y la fecha de ingreso del registro de actividad
            registrosPendientes = registrosPendientesService
                    .findByEfectorAndFecha(registroActividad.getEfector(), registroActividad.getFechaIngreso())
                    .get();
        } catch (Exception e) {
            // Si no se encuentra un registro pendiente, crea uno nuevo y establece sus atributos
            registrosPendientes.setEfector(registroActividad.getEfector());
            registrosPendientes.setFecha(registroActividad.getFechaIngreso());
            registrosPendientes.setActivo(true);
        }
        registrosPendientes.getRegistrosActividades().add(registroActividad);
        registrosPendientesService.save(registrosPendientes);
        registroActividad.setRegistrosPendientes(registrosPendientes);
        registroActividadService.save(registroActividad);
        return registroActividad;
    }

    public ResponseEntity<?> deleteRegistroActividad(RegistroActividad registroActividad) {
        Long id = registroActividad.getRegistrosPendientes().getId();
        try {
            if (!registrosPendientesService.activo(id))
                return new ResponseEntity(new Mensaje("No se encontraron registros pendientes"), HttpStatus.NOT_FOUND);
            RegistrosPendientes registrosPendientes = registrosPendientesService.findById(id).get();

            registroActividad.setRegistrosPendientes(null);
            registrosPendientes.getRegistrosActividades().remove(registroActividad);
            registrosPendientesService.save(registrosPendientes);

            // Si el listado de registros de actividad esta vacio, eliminar el registro de
            // pendientes
            if (registrosPendientes.getRegistrosActividades().isEmpty()) {
                registrosPendientes.setEfector(null);

                registrosPendientesService.deleteById(registrosPendientes.getId());
            }

            return new ResponseEntity(new Mensaje("Registro de Actividad eliminado"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Mensaje(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
