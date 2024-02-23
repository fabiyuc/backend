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
import io.micrometer.common.util.StringUtils;

@RestController
@RequestMapping("/registroActividad")
@CrossOrigin(origins = "http://localhost:4200")
public class RegistroActividadControlador {

    @Autowired
    RegistroActividadService registroActividadServicio;

    @GetMapping("/list")
    public ResponseEntity<List<RegistroActividad>> list() {
        // System.out.println("entra######################");
        List<RegistroActividad> list = registroActividadServicio.list();
        return new ResponseEntity<List<RegistroActividad>>(list, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody RegistroActividadDto registroActividadDto) {

        if (StringUtils.isBlank(registroActividadDto.getEstablecimiento()))
            return new ResponseEntity(new Mensaje("el establecimiento es obligatorio"), HttpStatus.BAD_REQUEST);

        /*
         * if (StringUtils.isBlank(registroActividadDto.getServicio()))
         * return new ResponseEntity(new Mensaje("el servicio es obligatorio"),
         * HttpStatus.BAD_REQUEST);
         */
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

        registroActividad.setEstablecimiento(registroActividadDto.getEstablecimiento());
        // registroActividad.setServicio(registroActividadDto.getServicio());
        registroActividad.setFechaIngreso(registroActividadDto.getFechaIngreso());
        registroActividad.setFechaEgreso(registroActividadDto.getFechaEgreso());
        registroActividad.setHoraIngreso(registroActividadDto.getHoraIngreso());
        registroActividad.setHoraEgreso(registroActividadDto.getHoraEgreso());
        registroActividad.setTipoGuardia(registroActividadDto.getTipoGuardia());
        registroActividadServicio.save(registroActividad);
        return new ResponseEntity(new Mensaje("Registro de Actividad creado"), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id,
            @RequestBody RegistroActividadDto registroActividadDto) {

        if (!registroActividadServicio.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        if (StringUtils.isBlank(registroActividadDto.getEstablecimiento()))
            return new ResponseEntity(new Mensaje("el establecimiento es obligatorio"), HttpStatus.BAD_REQUEST);

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
        RegistroActividad registroActividad = registroActividadServicio.findById(id).get();

        registroActividad.setEstablecimiento(registroActividadDto.getEstablecimiento());
        // registroActividad.setServicio(registroActividadDto.getServicio());
        registroActividad.setFechaIngreso(registroActividadDto.getFechaIngreso());
        registroActividad.setFechaEgreso(registroActividadDto.getFechaEgreso());
        registroActividad.setHoraIngreso(registroActividadDto.getHoraIngreso());
        registroActividad.setHoraEgreso(registroActividadDto.getHoraEgreso());
        registroActividad.setTipoGuardia(registroActividadDto.getTipoGuardia());
        registroActividadServicio.save(registroActividad);
        return new ResponseEntity(new Mensaje("Registro de Actividad actualizado"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        if (!registroActividadServicio.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        registroActividadServicio.deleteById(id);
        return new ResponseEntity<>(new Mensaje("Notificación eliminada"), HttpStatus.OK);

    }

}
