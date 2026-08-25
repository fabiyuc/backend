//controller/DistribucionGiraController
package com.guardias.backend.controller;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

import com.guardias.backend.dto.DistribucionGiraDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.cronogramaTentativo.CronogramaTentativoResquestDto;
import com.guardias.backend.entity.DistribucionGira;
import com.guardias.backend.service.DistribucionGiraService;
import com.guardias.backend.service.DistribucionHorariaService;

@RestController
@RequestMapping("/distribucionGira")
@CrossOrigin(origins = "http://localhost:4200")
public class DistribucionGiraController {

    @Autowired
    DistribucionGiraService distribucionGiraService;
    @Autowired
    DistribucionHorariaService distribucionHorariaService;

    @GetMapping("/list")
    public ResponseEntity<List<DistribucionGira>> list() {
        List<DistribucionGira> list = distribucionGiraService.findByActivoTrue().get();
        return new ResponseEntity<List<DistribucionGira>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<DistribucionGira>> listAll() {
        List<DistribucionGira> list = distribucionGiraService.findAll();
        return new ResponseEntity<List<DistribucionGira>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<DistribucionGira> getById(@PathVariable("id") Long id) {
        if (!distribucionGiraService.activo(id))
            return new ResponseEntity(new Mensaje("No existe la carga horaria"), HttpStatus.NOT_FOUND);
        DistribucionGira distribucionGira = distribucionGiraService.findById(id).get();
        return new ResponseEntity<DistribucionGira>(distribucionGira, HttpStatus.OK);
    }

    @GetMapping("/list/{fechaInicio}")
    public ResponseEntity<List<DistribucionGira>> getByFechainicio(
            @PathVariable("fechaInicio") LocalDate fechaInicio) {
        List<DistribucionGira> list = distribucionGiraService.findByFechaInicio(fechaInicio);
        return new ResponseEntity<List<DistribucionGira>>(list, HttpStatus.OK);
    }

    @GetMapping("/listByActivoByPersonAndFechaInicio/{idPersona}/{fechaInicio}")
    public ResponseEntity<List<DistribucionGira>> getByActivoFechaInicioAndPersona(
            @PathVariable("idPersona") Long idPersona,
            @PathVariable("fechaInicio") LocalDate fechaInicio) {
        List<DistribucionGira> list = distribucionGiraService.findByActivoAndPersonaAndFechaInicio(true,
                idPersona, fechaInicio);
        return new ResponseEntity<List<DistribucionGira>>(list, HttpStatus.OK);
    }

    @GetMapping("/listByActivoByPersonAndFechaInicioAndFechaFin/{idPersona}/{fechaInicio}/{fechaFinalizacion}")
    public ResponseEntity<List<DistribucionGira>> getByActivoFechaInicioAndFechaFinAndPersona(
            @PathVariable("idPersona") Long idPersona,
            @PathVariable("fechaInicio") LocalDate fechaInicio,
            @PathVariable("fechaFinalizacion") LocalDate fechaFinalizacion) {
        List<DistribucionGira> list = distribucionGiraService
                .findByActivoAndPersonaAndFechaInicioAndFechaFin(true,
                        idPersona, fechaInicio, fechaFinalizacion);
        return new ResponseEntity<List<DistribucionGira>>(list, HttpStatus.OK);
    }

    @GetMapping("/detailByActivoByPersonaAndFechaInicio/{idPersona}/{mes}/{anio}")
    public ResponseEntity<List<DistribucionGira>> getByActivoPersonaAndFechaInicio(
            @PathVariable("idPersona") Long idPersona,
            @PathVariable("mes") int mes,
            @PathVariable("anio") int anio) {

        List<DistribucionGira> distribuciones = distribucionGiraService
                .findByActivoPersonaAndFechaInicio(idPersona, mes, anio);

        if (distribuciones.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(distribuciones, HttpStatus.OK);
    }

    @GetMapping("/existsByActivoByPersonaAndFechaInicio/{idPersona}/{mes}/{anio}")
    public ResponseEntity<Boolean> existsByActivoPersonaAndFechaInicio(
            @PathVariable("idPersona") Long idPersona,
            @PathVariable("mes") int mes,
            @PathVariable("anio") int anio) {

        boolean exists = distribucionGiraService.existsByActivoPersonaAndFechaInicio(idPersona, mes, anio);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/detailefector/{idEfector}")
    public ResponseEntity<List<DistribucionGira>> getByEfector(@PathVariable("idEfector") Long idEfector) {
        if (!distribucionGiraService.existsByEfectorId(idEfector))
            return new ResponseEntity(new Mensaje("no existe la carga horaria"),
                    HttpStatus.NOT_FOUND);
        List<DistribucionGira> distribucionGira = distribucionGiraService.findByEfectorId(idEfector).get();
        return new ResponseEntity<>(distribucionGira, HttpStatus.OK);
    }

    /*
     * @GetMapping("/detailpersona/{idPersona}")
     * public ResponseEntity<List<DistribucionGira>>
     * getByPersona(@PathVariable("idPersona") Long idPersona) {
     * if (!distribucionGiraService.existsByPersonaId(idPersona))
     * return new ResponseEntity(new Mensaje("no existe la carga horaria"),
     * HttpStatus.NOT_FOUND);
     * List<DistribucionGira> distribucionGira =
     * distribucionGiraService.findByPersonaId(idPersona).get();
     * return new ResponseEntity<>(distribucionGira, HttpStatus.OK);
     * }
     */

    // Nueva implementación de getByPersona que filtra solo las distribuciones
    // activas
    @GetMapping("/detailpersona/{idPersona}")
    public ResponseEntity<List<DistribucionGira>> getByPersona(@PathVariable("idPersona") Long idPersona) {
        if (!distribucionGiraService.existsByPersonaId(idPersona)) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        // Obtener distribuciones y filtrar solo las activas
        List<DistribucionGira> distribucionGiraActivas = distribucionGiraService.findByPersonaId(idPersona)
                .orElse(Collections.emptyList())
                .stream()
                .filter(DistribucionGira::isActivo)
                .collect(Collectors.toList());

        return ResponseEntity.ok(distribucionGiraActivas);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody DistribucionGiraDto distribucionGiraDto) {

        ResponseEntity<?> respuestaValidaciones = distribucionHorariaService.validations(distribucionGiraDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            DistribucionGira distribucionGira = distribucionGiraService.createUpdate(new DistribucionGira(),
                    distribucionGiraDto);
            distribucionGiraService.save(distribucionGira);
            return new ResponseEntity(new Mensaje("Distribucion horaria creada"),
                    HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id,
            @RequestBody DistribucionGiraDto distribucionGiraDto) {

        if (!distribucionGiraService.activo(id))
            return new ResponseEntity(new Mensaje("La distribucion no existe"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = distribucionHorariaService.validations(distribucionGiraDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            DistribucionGira distribucionGira = distribucionGiraService.createUpdate(
                    distribucionGiraService.findById(id).get(),
                    distribucionGiraDto);
            distribucionGiraService.save(distribucionGira);
            return new ResponseEntity(new Mensaje("Distribucion horaria modificada correctamente"),
                    HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!distribucionGiraService.activo(id))
            return new ResponseEntity(new Mensaje("no existe la distribucion"), HttpStatus.NOT_FOUND);

        DistribucionGira distribucionGira = distribucionGiraService.findById(id).get();
        distribucionGira.setActivo(false);
        distribucionGiraService.save(distribucionGira);
        return new ResponseEntity(new Mensaje("distribucion eliminada correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") Long id) {

        if (!distribucionGiraService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la distribucion"), HttpStatus.NOT_FOUND);
        distribucionGiraService.deleteById(id);
        return new ResponseEntity(new Mensaje("distribucion eliminada FISICAMENTE"), HttpStatus.OK);
    }

    // busca distribucion gira para verificar si es igual al tentativo que recibe
    @PostMapping("/verificarCronogramaEnDistribucion")
    public boolean verificarCronogramaEnDistribucion(@RequestBody CronogramaTentativoResquestDto dto) {
        return distribucionGiraService.validarCronogramaEnDistribucion(dto);
    }
}