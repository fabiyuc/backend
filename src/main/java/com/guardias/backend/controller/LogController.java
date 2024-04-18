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

    @GetMapping("/list")
    public ResponseEntity<List<Log>> list() {
        List<Log> list = logService.findByActivoTrue().get();
        return new ResponseEntity<List<Log>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Log> getById(@PathVariable("id") Long id) {
        if (!logService.existsById(id))
            return new ResponseEntity(new Mensaje("No existe el log"), HttpStatus.NOT_FOUND);
        Log log = logService.findById(id).get();
        return new ResponseEntity<Log>(log, HttpStatus.OK);
    }

    private ResponseEntity<?> validations(LogDto logDto) {

        if (StringUtils.isBlank(logDto.getFecha().toString()))
            return new ResponseEntity<>(new Mensaje("la fecha es obligatoria"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(logDto.getSeccion()))
            return new ResponseEntity<>(new Mensaje("la seccion es obligatoria"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(logDto.getAccion()))
            return new ResponseEntity<>(new Mensaje("la accion es obligatoria"), HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);

    }

    private Log createUpdate(Log log, LogDto logDto) {

        if (log.getFecha() != logDto.getFecha())
            log.setFecha(logDto.getFecha());

        if (log.getSeccion() != logDto.getSeccion() && logDto.getSeccion() != null
                && !logDto.getSeccion().isEmpty())
            log.setSeccion(logDto.getSeccion());

        if (log.getAccion() != logDto.getAccion() && logDto.getAccion() != null
                && !logDto.getAccion().isEmpty())
            log.setAccion(logDto.getAccion());

        log.setActivo(true);

        return log;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody LogDto logDto) {

        ResponseEntity<?> respuestaValidaciones = validations(logDto);

        if (StringUtils.isBlank(logDto.getFecha().toString()))
            return new ResponseEntity<>(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Log log = createUpdate(new Log(), logDto);
            logService.save(log);
            return new ResponseEntity<>(new Mensaje("Log creado"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody LogDto logDto) {
        if (!logService.activo(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = validations(logDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Log log = createUpdate(logService.findById(id).get(), logDto);
            logService.save(log);
            return new ResponseEntity<>(new Mensaje("Log modificado correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!logService.activo(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        Log log = logService.findById(id).get();
        log.setActivo(false);
        logService.save(log);
        return new ResponseEntity<>(new Mensaje("Log eliminado correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") Long id) {
        if (!logService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        logService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("Log eliminado FISICAMENTE"), HttpStatus.OK);
    }
}
