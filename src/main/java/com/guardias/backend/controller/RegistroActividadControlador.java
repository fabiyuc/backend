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
import com.guardias.backend.service.RegistroActividadService;

@RestController
@RequestMapping("/registroActividad")
@CrossOrigin(origins = "http://localhost:4200")
public class RegistroActividadControlador {

    @Autowired
    RegistroActividadService registroActividadService;

    @GetMapping("/list")
    public ResponseEntity<List<RegistroActividad>> list() {
        List<RegistroActividad> list = registroActividadService.findByActivo();
        return new ResponseEntity<List<RegistroActividad>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<RegistroActividad>> listAll() {
        List<RegistroActividad> list = registroActividadService.findAll();
        return new ResponseEntity<List<RegistroActividad>>(list, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody RegistroActividadDto registroActividadDto) {

        if (registroActividadDto.getFechaIngreso() == null)
            return new ResponseEntity(new Mensaje("la fecha de ingreso es obligatoria"), HttpStatus.BAD_REQUEST);

        if (registroActividadDto.getFechaEgreso() == null)
            return new ResponseEntity(new Mensaje("la fecha de egreso es obligatoria"), HttpStatus.BAD_REQUEST);

        if (registroActividadDto.getHoraIngreso() == null)
            return new ResponseEntity(new Mensaje("la hora de ingreso es obligatoria"), HttpStatus.BAD_REQUEST);

        if (registroActividadDto.getHoraEgreso() == null)
            return new ResponseEntity(new Mensaje("la hora de egreso es obligatoria"), HttpStatus.BAD_REQUEST);

        /*
         * if (registroActividadServicio.existsByNombre(profesionalDto.getNombre()))
         * return new ResponseEntity(new Mensaje("ese nombre ya existe"),
         * HttpStatus.BAD_REQUEST);
         */
        RegistroActividad registroActividad = new RegistroActividad();

        // registroActividad.setServicio(registroActividadDto.getServicio());
        registroActividad.setFechaIngreso(registroActividadDto.getFechaIngreso());
        registroActividad.setFechaEgreso(registroActividadDto.getFechaEgreso());
        registroActividad.setHoraIngreso(registroActividadDto.getHoraIngreso());
        registroActividad.setHoraEgreso(registroActividadDto.getHoraEgreso());
        registroActividad.setTipoGuardia(registroActividadDto.getTipoGuardia());
        registroActividadService.save(registroActividad);
        return new ResponseEntity(new Mensaje("Registro de Actividad creado"), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id,
            @RequestBody RegistroActividadDto registroActividadDto) {

        if (!registroActividadService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        if (registroActividadDto.getFechaIngreso() == null)
            return new ResponseEntity(new Mensaje("la fecha de ingreso es obligatoria"), HttpStatus.BAD_REQUEST);

        if (registroActividadDto.getFechaEgreso() == null)
            return new ResponseEntity(new Mensaje("la fecha de egreso es obligatoria"), HttpStatus.BAD_REQUEST);

        if (registroActividadDto.getHoraIngreso() == null)
            return new ResponseEntity(new Mensaje("la hora de ingreso es obligatoria"), HttpStatus.BAD_REQUEST);

        if (registroActividadDto.getHoraEgreso() == null)
            return new ResponseEntity(new Mensaje("la hora de egreso es obligatoria"), HttpStatus.BAD_REQUEST);

        /*
         * if (registroActividadServicio.existsByNombre(profesionalDto.getNombre()))
         * return new ResponseEntity(new Mensaje("ese nombre ya existe"),
         * HttpStatus.BAD_REQUEST);
         */
        RegistroActividad registroActividad = registroActividadService.findById(id).get();

        // registroActividad.setServicio(registroActividadDto.getServicio());
        registroActividad.setFechaIngreso(registroActividadDto.getFechaIngreso());
        registroActividad.setFechaEgreso(registroActividadDto.getFechaEgreso());
        registroActividad.setHoraIngreso(registroActividadDto.getHoraIngreso());
        registroActividad.setHoraEgreso(registroActividadDto.getHoraEgreso());
        registroActividad.setTipoGuardia(registroActividadDto.getTipoGuardia());
        registroActividadService.save(registroActividad);
        return new ResponseEntity(new Mensaje("Registro de Actividad actualizado"), HttpStatus.OK);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!registroActividadService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        RegistroActividad registroActividad = registroActividadService.findById(id).get();
        registroActividad.setActivo(false);
        registroActividadService.save(registroActividad);
        return new ResponseEntity<>(new Mensaje("Notificación eliminada correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!registroActividadService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        registroActividadService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("Notificación eliminada FISICAMENTEE"), HttpStatus.OK);
    }

}
