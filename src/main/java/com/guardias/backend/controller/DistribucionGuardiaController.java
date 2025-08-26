package com.guardias.backend.controller;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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

import com.guardias.backend.dto.DistribucionGuardiaDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.cronogramaTentativo.CronogramaTentativoResquestDto;
import com.guardias.backend.dto.cronogramaTentativo.ValidacionCronogramaResponseDto;
import com.guardias.backend.dto.distribucionGuardia.DistribucionCheckDto;
import com.guardias.backend.dto.novedadPersonal.ConsultaLicenciaCompensatorioDto;
import com.guardias.backend.entity.DistribucionGuardia;
import com.guardias.backend.entity.DistribucionHoraria;
import com.guardias.backend.enums.DiasEnum;
import com.guardias.backend.service.DistribucionGuardiaService;
import com.guardias.backend.service.ServicioService;

@RestController
@RequestMapping("/distribucionGuardia")
@CrossOrigin(origins = "http://localhost:4200")
public class DistribucionGuardiaController {

    @Autowired
    DistribucionGuardiaService distribucionGuardiaService;
    @Autowired
    DistribucionHorariaController distribucionHorariaController;
    @Autowired
    ServicioService servicioService;

    @GetMapping("/list")
    public ResponseEntity<List<DistribucionGuardia>> list() {
        List<DistribucionGuardia> list = distribucionGuardiaService.findByActivoTrue().get();
        return new ResponseEntity<List<DistribucionGuardia>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<DistribucionGuardia>> listAll() {
        List<DistribucionGuardia> list = distribucionGuardiaService.findAll();
        return new ResponseEntity<List<DistribucionGuardia>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<DistribucionGuardia> getById(@PathVariable("id") Long id) {
        if (!distribucionGuardiaService.activo(id))
            return new ResponseEntity(new Mensaje("No existe la carga horaria"), HttpStatus.NOT_FOUND);
        DistribucionGuardia distribucionGuardia = distribucionGuardiaService.findById(id).get();
        return new ResponseEntity<DistribucionGuardia>(distribucionGuardia, HttpStatus.OK);
    }

    @GetMapping("/list/{fechaInicio}")
    public ResponseEntity<List<DistribucionGuardia>> getByFechainicio(
            @PathVariable("fechaInicio") LocalDate fechaInicio) {
        List<DistribucionGuardia> list = distribucionGuardiaService.findByFechaInicio(fechaInicio);
        return new ResponseEntity<List<DistribucionGuardia>>(list, HttpStatus.OK);
    }

    @GetMapping("/listByActivoByPersonAndFechaInicio/{idPersona}/{fechaInicio}")
    public ResponseEntity<List<DistribucionGuardia>> getByActivoFechaInicioAndPersona(
            @PathVariable("idPersona") Long idPersona,
            @PathVariable("fechaInicio") LocalDate fechaInicio) {
        List<DistribucionGuardia> list = distribucionGuardiaService.findByActivoAndPersonaAndFechaInicio(true,
                idPersona, fechaInicio);
        return new ResponseEntity<List<DistribucionGuardia>>(list, HttpStatus.OK);
    }

    @GetMapping("/listByActivoByPersonAndFechaInicioAndFechaFin/{idPersona}/{fechaInicio}/{fechaFinalizacion}")
    public ResponseEntity<List<DistribucionGuardia>> getByActivoFechaInicioAndFechaFinAndPersona(
            @PathVariable("idPersona") Long idPersona,
            @PathVariable("fechaInicio") LocalDate fechaInicio,
            @PathVariable("fechaFinalizacion") LocalDate fechaFinalizacion) {
        List<DistribucionGuardia> list = distribucionGuardiaService
                .findByActivoAndPersonaAndFechaInicioAndFechaFin(true,
                        idPersona, fechaInicio, fechaFinalizacion);
        return new ResponseEntity<List<DistribucionGuardia>>(list, HttpStatus.OK);
    }

    @GetMapping("/detailByActivoByPersonaAndFechaInicio/{idPersona}/{mes}/{anio}")
    public ResponseEntity<List<DistribucionGuardia>> getByActivoPersonaAndFechaInicio(
            @PathVariable("idPersona") Long idPersona,
            @PathVariable("mes") int mes,
            @PathVariable("anio") int anio) {

        List<DistribucionGuardia> distribuciones = distribucionGuardiaService
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

        boolean exists = distribucionGuardiaService.existsByActivoPersonaAndFechaInicio(idPersona, mes, anio);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/detailefector/{idEfector}")
    public ResponseEntity<List<DistribucionGuardia>> getByEfector(@PathVariable("idEfector") Long idEfector) {
        if (!distribucionGuardiaService.existsByEfectorId(idEfector))
            return new ResponseEntity(new Mensaje("no existe la carga horaria"),
                    HttpStatus.NOT_FOUND);
        List<DistribucionGuardia> distribucionGuardia = distribucionGuardiaService.findByEfectorId(idEfector).get();
        return new ResponseEntity<>(distribucionGuardia, HttpStatus.OK);
    }

    @GetMapping("/detailpersona/{idPersona}")
    public ResponseEntity<List<DistribucionGuardia>> getByPersona(@PathVariable("idPersona") Long idPersona) {
        // Si la persona no tiene distribuciones, devolvemos un 200 OK con una lista
        // vacía
        if (!distribucionGuardiaService.existsByPersonaId(idPersona)) {
            return ResponseEntity.ok(Collections.emptyList()); // <-- SOLUCIÓN AQUÍ
        }

        // Obtener distribuciones y filtrar solo las activas
        List<DistribucionGuardia> distribucionGuardiaActivas = distribucionGuardiaService.findByPersonaId(idPersona)
                .orElse(Collections.emptyList()) // Si el Optional está vacío, devuelve lista vacía
                .stream()
                .filter(DistribucionGuardia::isActivo) // Filtra solo las distribuciones activas
                .collect(Collectors.toList()); // Convierte el resultado a lista

        return ResponseEntity.ok(distribucionGuardiaActivas);
    }

    DistribucionGuardia createUpdate(DistribucionGuardia distribucionGuardia,
            DistribucionGuardiaDto distribucionGuardiaDto) {

        DistribucionHoraria distribucionHoraria = distribucionHorariaController.createUpdate(distribucionGuardia,
                distribucionGuardiaDto);
        distribucionGuardia = (DistribucionGuardia) distribucionHoraria;

        if (distribucionGuardiaDto.getTipoGuardia() != distribucionGuardia.getTipoGuardia()
                && distribucionGuardiaDto.getTipoGuardia() != null)
            distribucionGuardia.setTipoGuardia(distribucionGuardiaDto.getTipoGuardia());

        if (distribucionGuardia.getServicio() == null ||
                (distribucionGuardiaDto.getIdServicio() != null &&
                        !Objects.equals(distribucionGuardia.getServicio().getId(),
                                distribucionGuardiaDto.getIdServicio()))) {
            distribucionGuardia.setServicio(servicioService.findById(distribucionGuardiaDto.getIdServicio()).get());
        }

        distribucionGuardia.setActivo(true);

        return distribucionGuardia;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody DistribucionGuardiaDto distribucionGuardiaDto) {

        ResponseEntity<?> respuestaValidaciones = distribucionHorariaController.validations(distribucionGuardiaDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            DistribucionGuardia distribucionGuardia = createUpdate(new DistribucionGuardia(),
                    distribucionGuardiaDto);
            distribucionGuardiaService.save(distribucionGuardia);
            return new ResponseEntity(new Mensaje("Distribucion horaria creada"),
                    HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id,
            @RequestBody DistribucionGuardiaDto distribucionGuardiaDto) {

        if (!distribucionGuardiaService.activo(id))
            return new ResponseEntity(new Mensaje("La distribucion no existe"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = distribucionHorariaController.validations(distribucionGuardiaDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            DistribucionGuardia distribucionGuardia = createUpdate(
                    distribucionGuardiaService.findById(id).get(),
                    distribucionGuardiaDto);
            distribucionGuardiaService.save(distribucionGuardia);
            return new ResponseEntity(new Mensaje("Distribucion horaria modificada correctamente"),
                    HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!distribucionGuardiaService.activo(id))
            return new ResponseEntity(new Mensaje("no existe la distribucion"), HttpStatus.NOT_FOUND);

        DistribucionGuardia distribucionGuardia = distribucionGuardiaService.findById(id).get();
        distribucionGuardia.setActivo(false);
        distribucionGuardiaService.save(distribucionGuardia);
        return new ResponseEntity(new Mensaje("distribucion eliminada correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") Long id) {

        if (!distribucionGuardiaService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la distribucion"), HttpStatus.NOT_FOUND);
        distribucionGuardiaService.deleteById(id);
        return new ResponseEntity(new Mensaje("distribucion eliminada FISICAMENTE"), HttpStatus.OK);
    }

    // consulta si existe una distribucion de tipo Guardia o Consultorio
    @GetMapping("/existDistribucion/{dia}/{fecha}/{idAsistencial}/{idEfector}")
    public boolean existDistribucion(@PathVariable("dia") DiasEnum dia, @PathVariable("fecha") LocalDate fecha,
            @PathVariable("idAsistencial") long idAsistencial, @PathVariable("idEfector") long idEfector) {
        return distribucionGuardiaService.existDistribucion(dia, fecha, idAsistencial, idEfector);
    }

    // busca distribucion guardia para comparar con cronograma tentativo
    @PostMapping("/verificarCronogramaEnDistribucion")
    public ResponseEntity<ValidacionCronogramaResponseDto> verificarCronogramaEnDistribucion(
            @RequestBody CronogramaTentativoResquestDto dto) {

        System.out.println("=== INICIO verificarCronogramaEnDistribucion ===");
        System.out.println("Request recibido: " + dto.toString());

        ValidacionCronogramaResponseDto response = distribucionGuardiaService.validarCronogramaEnDistribucion(dto);

        System.out.println("Response generado: " + response.toString());
        System.out.println("=== FIN verificarCronogramaEnDistribucion ===");

        return ResponseEntity.ok(response);
    }

    // verifica si existe alguna distribucion activa en esa semana
    @PostMapping("/validarDistribucionSemanal")
    public boolean validarDistribucionSemanal(
            @RequestBody CronogramaTentativoResquestDto dto) {

        return distribucionGuardiaService.tieneDistribucionEnSemana(dto);
    }

    @GetMapping("/esGuardia/{dia}/{fecha}/{idAsistencial}/{idEfector}")
    public boolean esGuardia(@PathVariable("dia") DiasEnum dia, @PathVariable("fecha") LocalDate fecha,
            @PathVariable("idAsistencial") long idAsistencial, @PathVariable("idEfector") long idEfector) {
        return distribucionGuardiaService.esGuardia(dia, fecha, idAsistencial, idEfector);
    }

    @PostMapping("/tieneDistribucionActiva")
    public ResponseEntity<Boolean> tieneDistribucionActiva(@RequestBody DistribucionCheckDto request) {
        boolean existe = distribucionGuardiaService.tieneDistribucionActiva(request);
        return ResponseEntity.ok(existe);
    }

    @PostMapping("/verificarSuperposicionConCargo")
    public ResponseEntity<Boolean> verificarSuperposicionConCargo(@RequestBody ConsultaLicenciaCompensatorioDto dto) {
        boolean existeSuperposicion = distribucionGuardiaService.existeSuperposicionConCargo(
                dto.getIdPersona(),
                dto.getFechaInicioConsulta(),
                dto.getFechaFinConsulta(),
                dto.getHoraInicioConsulta(),
                dto.getHoraFinConsulta());

        return ResponseEntity.ok(existeSuperposicion);
    }
}