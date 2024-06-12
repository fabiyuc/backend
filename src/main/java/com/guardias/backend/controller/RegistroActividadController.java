package com.guardias.backend.controller;

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

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.RegistroActividadDto;
import com.guardias.backend.entity.RegistroActividad;
import com.guardias.backend.security.service.UsuarioService;
import com.guardias.backend.service.AsistencialService;
import com.guardias.backend.service.EfectorService;
import com.guardias.backend.service.RegistroActividadService;
import com.guardias.backend.service.RegistroMensualService;
import com.guardias.backend.service.RegistrosPendientesService;
import com.guardias.backend.service.ServicioService;
import com.guardias.backend.service.TipoGuardiaService;

@RestController
@RequestMapping("/registroActividad")
@CrossOrigin(origins = "http://localhost:4200")
public class RegistroActividadController {

    @Autowired
    RegistroActividadService registroActividadService;
    @Autowired
    ServicioService servicioService;
    @Autowired
    EfectorService efectorService;
    @Autowired
    AsistencialService asistencialService;
    @Autowired
    TipoGuardiaService tipoGuardiaService;

    @Autowired
    RegistroMensualService registroMensualService;

    @Autowired
    EfectorController efectorController;

    @Autowired
    RegistrosPendientesService registrosPendientesService;
    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/list")
    public ResponseEntity<List<RegistroActividad>> list() {
        List<RegistroActividad> list = registroActividadService.findByActivoTrue().get();
        return new ResponseEntity<List<RegistroActividad>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<RegistroActividad>> listAll() {
        List<RegistroActividad> list = registroActividadService.findAll();
        return new ResponseEntity<List<RegistroActividad>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<RegistroActividad>> getById(@PathVariable("id") Long id) {
        if (!registroActividadService.activo(id))
            return new ResponseEntity(new Mensaje("El registro de actividad no existe"), HttpStatus.NOT_FOUND);
        RegistroActividad registroActividad = registroActividadService.findById(id).get();
        return new ResponseEntity(registroActividad, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody RegistroActividadDto registroActividadDto) {

        ResponseEntity<?> respuestaValidaciones = registroActividadService.validations(registroActividadDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            RegistroActividad registroActividad = registroActividadService.createUpdate(new RegistroActividad(),
                    registroActividadDto);

            registroActividad = registrosPendientesService.addRegistroActividad(registroActividad);
            registroActividadService.save(registroActividad);

            if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
                return new ResponseEntity(new Mensaje("Registro de Actividad creado"), HttpStatus.OK);
            } else {
                return new ResponseEntity(new Mensaje("error"), HttpStatus.BAD_REQUEST);
            }
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id,
            @RequestBody RegistroActividadDto registroActividadDto) {
        if (!registroActividadService.activo(id))
            return new ResponseEntity(new Mensaje("Registro de actividad no existe"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = registroActividadService.validations(registroActividadDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            RegistroActividad registroActividad = registroActividadService.createUpdate(
                    registroActividadService.findById(id).get(),
                    registroActividadDto);
            registroActividadService.save(registroActividad);
            return new ResponseEntity(new Mensaje("Registro de Actividad modificada"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    // VER que tipo de registro recibirá desde el front para la salida
    @PutMapping("/registrarSalida/{id}")
    public ResponseEntity<?> registrarSalida(@PathVariable("id") Long id,
            @RequestBody RegistroActividadDto registroActividadDto) {

        if (!registroActividadService.activo(id))
            return new ResponseEntity(new Mensaje("Registro de actividad no existe"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> registrarSalida = registroActividadService.registrarSalida(id, registroActividadDto);

        return registrarSalida;
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!registroActividadService.activo(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        return registroActividadService.logicDelete(id);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!registroActividadService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        registroActividadService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("Registro de actividad eliminada FISICAMENTEE"), HttpStatus.OK);
    }

}
