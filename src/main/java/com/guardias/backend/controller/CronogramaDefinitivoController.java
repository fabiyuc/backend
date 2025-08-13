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

import com.guardias.backend.dto.CronogramaDefinitivoDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.cronogramaDefinitivo.CronogramaDefinitivoListDto;
import com.guardias.backend.entity.CronogramaDefinitivo;
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.service.CronogramaDefinitivoService;

@RestController
@RequestMapping("/cronogramaDefinitivo")
@CrossOrigin(origins = "http://localhost:4200")
public class CronogramaDefinitivoController {

    @Autowired
    CronogramaDefinitivoService cronogramaDefinitivoService;

    @GetMapping("/list")
    public ResponseEntity<List<CronogramaDefinitivo>> list() {
        List<CronogramaDefinitivo> list = cronogramaDefinitivoService.findByActivoTrue().get();
        return new ResponseEntity<List<CronogramaDefinitivo>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<CronogramaDefinitivo>> listAll() {
        List<CronogramaDefinitivo> list = cronogramaDefinitivoService.findAll();
        return new ResponseEntity<List<CronogramaDefinitivo>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<CronogramaDefinitivo>> getById(@PathVariable("id") Long id) {
        if (!cronogramaDefinitivoService.activo(id))
            return new ResponseEntity(new Mensaje("El cronograma definitivo no existe"), HttpStatus.NOT_FOUND);
        CronogramaDefinitivo cronogramaDefinitivo = cronogramaDefinitivoService.findById(id).get();
        return new ResponseEntity(cronogramaDefinitivo, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CronogramaDefinitivoDto cronogramaDefinitivoDto) {
        ResponseEntity<?> respuestaValidaciones = cronogramaDefinitivoService.validations(cronogramaDefinitivoDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            CronogramaDefinitivo cronogramaDefinitivo = cronogramaDefinitivoService
                    .createUpdate(new CronogramaDefinitivo(), cronogramaDefinitivoDto);
            cronogramaDefinitivoService.save(cronogramaDefinitivo);
            return new ResponseEntity(new Mensaje("Cronograma definitivo creado"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id,
            @RequestBody CronogramaDefinitivoDto cronogramaDefinitivoDto) {
        if (!cronogramaDefinitivoService.activo(id))
            return new ResponseEntity(new Mensaje("Cronograma definitivo no existe"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = cronogramaDefinitivoService.validations(cronogramaDefinitivoDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            CronogramaDefinitivo cronogramaDefinitivo = cronogramaDefinitivoService.createUpdate(
                    cronogramaDefinitivoService.findById(id).get(),
                    cronogramaDefinitivoDto);
            cronogramaDefinitivoService.save(cronogramaDefinitivo);
            return new ResponseEntity(new Mensaje("Cronograma definitivo modificado"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!cronogramaDefinitivoService.activo(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        CronogramaDefinitivo cronogramaDefinitivo = cronogramaDefinitivoService.findById(id).get();
        cronogramaDefinitivo.setActivo(false);
        cronogramaDefinitivoService.save(cronogramaDefinitivo);
        return new ResponseEntity<>(new Mensaje("Cronograma definitivo eliminado correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!cronogramaDefinitivoService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        cronogramaDefinitivoService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("cronograma definitivo eliminado FISICAMENTEE"), HttpStatus.OK);
    }

    @GetMapping("/listCronogramaByAnioMesEfector/{anio}/{mes}/{idEfector}")
    public ResponseEntity<List<CronogramaDefinitivo>> listCronograma(
            @PathVariable int anio,
            @PathVariable String mes,
            @PathVariable Long idEfector) {
        MesesEnum mesEnum = MesesEnum.valueOf(mes);
        try {
            List<CronogramaDefinitivo> cronogramas = cronogramaDefinitivoService
                    .findByAnioAndMesAndIdEfectorAndActivoTrue(anio, mesEnum, idEfector);
            return new ResponseEntity<>(cronogramas, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/listCronogramaByAnioMesEfectorGuardia/{anio}/{mes}/{idEfector}/{idTipoGuardia}")
    public ResponseEntity<List<CronogramaDefinitivoListDto>> listByAnioMesEfectorAndTipoGuardia(
            @PathVariable int anio,
            @PathVariable String mes,
            @PathVariable Long idEfector,
            @PathVariable Long idTipoGuardia) {
        
        MesesEnum mesEnum = MesesEnum.valueOf(mes);
        try {
            List<CronogramaDefinitivoListDto> cronogramas = cronogramaDefinitivoService
                    .findByAnioMesIdEfectorTipoGuardiaAndActivoTrue(anio, mesEnum, idEfector, idTipoGuardia);
            return new ResponseEntity<>(cronogramas, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

}
