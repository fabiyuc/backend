package com.guardias.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guardias.backend.dto.CronogramaTentativoDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.CronogramaTentativo;
import com.guardias.backend.service.CronogramaTentativoService;

import jakarta.validation.ValidationException;

@RestController
@RequestMapping("/cronogramaTentativo")
@CrossOrigin(origins = "http://localhost:4200")
public class CronogramaTentativoController {
    
    @Autowired
    CronogramaTentativoService cronogramaTentativoService;

    @GetMapping("/list")
    public ResponseEntity<List<CronogramaTentativo>> list() {
        List<CronogramaTentativo> list = cronogramaTentativoService.findByActivoTrue().get();
        return new ResponseEntity<List<CronogramaTentativo>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<CronogramaTentativo>> listAll() {
        List<CronogramaTentativo> list = cronogramaTentativoService.findAll();
        return new ResponseEntity<List<CronogramaTentativo>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<CronogramaTentativo>> getById(@PathVariable("id") Long id) {
        if (!cronogramaTentativoService.activo(id))
            return new ResponseEntity(new Mensaje("El cronograma tentativo no existe"), HttpStatus.NOT_FOUND);
            CronogramaTentativo cronogramaTentativo = cronogramaTentativoService.findById(id).get();
        return new ResponseEntity(cronogramaTentativo, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CronogramaTentativoDto cronogramaTentativoDto) {

        ResponseEntity<?> respuestaValidaciones = cronogramaTentativoService.validations(cronogramaTentativoDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            CronogramaTentativo cronogramaTentativo = cronogramaTentativoService.createUpdate(new CronogramaTentativo(),
                    cronogramaTentativoDto);
            
            cronogramaTentativoService.save(cronogramaTentativo);

            if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
                return new ResponseEntity(new Mensaje("Cronograma tentativo creado"), HttpStatus.OK);
            } else {
                return new ResponseEntity(new Mensaje("error"), HttpStatus.BAD_REQUEST);
            }
        } else {
            return respuestaValidaciones;
        }
    }

    //falta el update, donde tiene que hacer igual que en el create de valorGmicontroller

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id, @RequestBody String observacion) {

        try {
            // Verifica que los valores requeridos estén presentes
            if (observacion == null || observacion.isBlank()) {
                return new ResponseEntity<>(new Mensaje("Es obligatorio indicar una observacion"), HttpStatus.BAD_REQUEST);
            }

            cronogramaTentativoService.logicDelete(id, observacion);
            return new ResponseEntity<>(new Mensaje("Cronograma tentativo dado de baja lógicamente"), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new Mensaje(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (ValidationException e) {
            return new ResponseEntity<>(new Mensaje(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /* // busca cronograma tentativo para comparar con registro de actividad
    @PostMapping("/verificarCronogramaEnDistribucion")
    public boolean verificarCronogramaEnDistribucion(@RequestBody CronogramaTentativoResquestDto dto) {
        
        return distribucionGuardiaService.validarCronogramaEnDistribucion(dto);
    } */

}
