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

import com.guardias.backend.dto.LogDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Log;
import com.guardias.backend.service.LogService;

import io.micrometer.common.util.StringUtils;

@RestController
@RequestMapping("/log")
@CrossOrigin(origins = "http://localhost:4200")
public class LogController {

    @Autowired
    LogService logService;

    @GetMapping("/lista")
    public ResponseEntity<List<Log>> list() {
        List<Log> list = logService.list();
        return new ResponseEntity<List<Log>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Log> getById(@PathVariable("id") Long id) {
        if (!logService.existsById(id))
            return new ResponseEntity(new Mensaje("No existe el log"), HttpStatus.NOT_FOUND);
        Log log = logService.getById(id).get();
        return new ResponseEntity<Log>(log, HttpStatus.OK);
    }

    @GetMapping("/detaille/{nombre}")
    public ResponseEntity<Log> getByDni(@PathVariable("nombre") String nombre) {
        if (!logService.existsByNombre(nombre))
            return new ResponseEntity(new Mensaje("no existe el log"), HttpStatus.NOT_FOUND);
        Log log = logService.getByNombre(nombre).get();
        return new ResponseEntity<Log>(log, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody LogDto logDto) {
        if (StringUtils.isBlank(logDto.getFecha().toString()))
            return new ResponseEntity<>(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        if (logService.existsByNombre(logDto.getSeccion()))
            return new ResponseEntity(new Mensaje("El id ya existe"), HttpStatus.BAD_REQUEST);

        if (logService.existsByNombre(logDto.getAccion()))
            return new ResponseEntity(new Mensaje("ese nombre ya existe"), HttpStatus.BAD_REQUEST);
        Log log = new Log();
        log.setFecha(logDto.getFecha());
        log.setSeccion(logDto.getSeccion());
        log.setAccion(logDto.getAccion());
        logService.save(log);
        return new ResponseEntity<>(new Mensaje("Log creado"), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody LogDto logDto) {
        if (!logService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        if (StringUtils.isBlank(logDto.getFecha().toString()))
            return new ResponseEntity<>(new Mensaje("la fecha es obligatoria"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(logDto.getSeccion()))
            return new ResponseEntity<>(new Mensaje("la seccion es obligatoria"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(logDto.getAccion()))
            return new ResponseEntity<>(new Mensaje("la accion es obligatoria"), HttpStatus.BAD_REQUEST);

        Log log = logService.getById(id).get();
        log.setFecha(logDto.getFecha());
        log.setSeccion(logDto.getSeccion());
        log.setAccion(logDto.getAccion());
        logService.save(log);
        return new ResponseEntity<>(new Mensaje("Log Actualizado"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        if (!logService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        logService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("Log eliminado"), HttpStatus.OK);

    }

}
