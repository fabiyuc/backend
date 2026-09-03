package com.guardias.backend.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import com.guardias.backend.dto.cronogramaTentativo.AutorizadoUpdateDto;
import com.guardias.backend.dto.cronogramaTentativo.CronogramaTentativoListAtorizadoDto;
import com.guardias.backend.dto.cronogramaTentativo.CronogramaTentativoServicioDto;
import com.guardias.backend.dto.cronogramaTentativo.CronogramaTentativoSummaryDto;
import com.guardias.backend.dto.cronogramaTentativo.TentativoIdsResponseDto;
import com.guardias.backend.dto.cronogramaTentativo.TentativoSearchRequestDto;
import com.guardias.backend.dto.cronogramaTentativo.VerificacionTentativoResponseDto;
import com.guardias.backend.dto.registroActividad.RegActivRegIngresoDto;
import com.guardias.backend.entity.CronogramaTentativo;
import com.guardias.backend.enums.AutorizadoTentativoEnum;
import com.guardias.backend.service.CronogramaTentativoService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
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

    @GetMapping("/listByEfectorAndAutorizadoFalse/{idEfector}")
    public ResponseEntity<List<CronogramaTentativoSummaryDto>> listByEfectorAndAutorizadoFalse(
            @PathVariable Long idEfector) {
        Optional<List<CronogramaTentativoSummaryDto>> result = cronogramaTentativoService
                .findByEfectorIdAndActivoTrueAndAutorizadoFalse(idEfector);

        return result.map(tentativos -> {
            if (tentativos.isEmpty()) {
                return new ResponseEntity<List<CronogramaTentativoSummaryDto>>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(tentativos, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @GetMapping("/listAnuladosByEfector/{idEfector}")
    public ResponseEntity<List<CronogramaTentativo>> listAnuladosByEfector(@PathVariable Long idEfector) {
        List<CronogramaTentativo> list = cronogramaTentativoService.findAnuladosByEfectorId(idEfector)
                .orElse(new ArrayList<>());

        if (list.isEmpty()) {
            return new ResponseEntity(new Mensaje("El efector no tiene cronogramas tentativos anulados"),
                    HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<List<CronogramaTentativo>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAnuladosByEfectorAndAsistencial/{fechaInicio}/{idAsistencial}/{idEfector}")
    public ResponseEntity<List<CronogramaTentativo>> listAnuladosByEfectorAndAsistencial(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @PathVariable Long idAsistencial, @PathVariable Long idEfector) {
        List<CronogramaTentativo> list = cronogramaTentativoService
                .findAnuladosByEfectorAndAsistencial(fechaInicio, idAsistencial, idEfector)
                .orElse(new ArrayList<>());

        if (list.isEmpty()) {
            return new ResponseEntity(new Mensaje("El efector y asistencial no tienen cronogramas tentativos anulados"),
                    HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<List<CronogramaTentativo>>(list, HttpStatus.OK);
    }

    @GetMapping("/detailByEfector/{idEfector}")
    public ResponseEntity<List<CronogramaTentativo>> getByEfector(@PathVariable("idEfector") Long idEfector) {
        List<CronogramaTentativo> cronogramaTentativo = cronogramaTentativoService.findByEfectorId(idEfector)
                .orElse(new ArrayList<>());

        if (cronogramaTentativo.isEmpty()) {
            return new ResponseEntity(new Mensaje("El efector no tiene cronograma tentativo activo"),
                    HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(cronogramaTentativo, HttpStatus.OK);
    }

    @GetMapping("/detailByEfectorAndServicio/{idEfector}/{idServicio}")
    public ResponseEntity<List<CronogramaTentativo>> getByEfectorAndServicio(
            @PathVariable("idEfector") Long idEfector,
            @PathVariable("idServicio") Long idServicio) {

        List<CronogramaTentativo> cronogramaTentativo = cronogramaTentativoService
                .findByEfectorAndServicio(idEfector, idServicio)
                .orElse(new ArrayList<>());

        return new ResponseEntity<>(cronogramaTentativo, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<CronogramaTentativo>> getById(@PathVariable("id") Long id) {
        if (!cronogramaTentativoService.activo(id))
            return new ResponseEntity(new Mensaje("El cronograma tentativo no existe"), HttpStatus.NOT_FOUND);
        CronogramaTentativo cronogramaTentativo = cronogramaTentativoService.findById(id).get();
        return new ResponseEntity(cronogramaTentativo, HttpStatus.OK);
    }

    @GetMapping("/detailByIdAsistencial/{idAsistencial}")
    public ResponseEntity<List<CronogramaTentativo>> getByIdAsistencial(
            @PathVariable("idAsistencial") Long idAsistencial) {
        List<CronogramaTentativo> cronogramasTentativos = cronogramaTentativoService.findByIdAsistencial(idAsistencial)
                .get();
        return ResponseEntity.ok(cronogramasTentativos);
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

    @GetMapping("/existenCronogramasDesdeFecha/{fechaInicio}/{idAsistencial}/{idEfector}")
    public ResponseEntity<Boolean> existenCronogramasDesdeFecha(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @PathVariable Long idAsistencial,
            @PathVariable Long idEfector) {
        boolean existen = cronogramaTentativoService.existenCronogramasDesdeFecha(fechaInicio, idAsistencial,
                idEfector);
        return ResponseEntity.ok(existen);
    }

    @PostMapping("/updateCronogramasDesdeFecha/{fechaInicio}/{idAsistencial}/{idEfector}")
    public ResponseEntity<?> updateCronogramasDesdeFecha(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @PathVariable Long idAsistencial,
            @PathVariable Long idEfector) {

        boolean actualizado = cronogramaTentativoService.updateCronogramasDesdeFecha(fechaInicio, idAsistencial,
                idEfector);

        if (actualizado) {
            return new ResponseEntity<>(new Mensaje("Cronogramas actualizados correctamente"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Mensaje("No se encontraron cronogramas en el rango de fechas"),
                    HttpStatus.NOT_FOUND);
        }
    }

    // falta el update, donde tiene que hacer igual que en el create de
    // valorGmicontroller

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id, @RequestBody String observacion) {

        try {
            // Verifica que los valores requeridos estén presentes
            if (observacion == null || observacion.isBlank()) {
                return new ResponseEntity<>(new Mensaje("Es obligatorio indicar una observacion"),
                        HttpStatus.BAD_REQUEST);
            }

            cronogramaTentativoService.logicDelete(id, observacion);
            return new ResponseEntity<>(new Mensaje("Cronograma tentativo dado de baja lógicamente"), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new Mensaje(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (ValidationException e) {
            return new ResponseEntity<>(new Mensaje(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    // verifica si existe un cronograma tentativo
    @PostMapping("/existCronograma")
    public boolean existCronograma(@RequestBody CronogramaTentativoDto dto) {
        return cronogramaTentativoService.existCronograma(dto);
    }

    @PostMapping("/existCronogramaConEfector")
    public ResponseEntity<List<Long>> efectoresConCronograma(@RequestBody CronogramaTentativoDto dto) {
        List<Long> efectores = cronogramaTentativoService.efectoresConCronogramaSuperpuesto(dto);
        return ResponseEntity.ok(efectores);
    }

    @PutMapping("/autorizar/{id}")
    public ResponseEntity<?> autorizar(@PathVariable("id") Long id) {

        try {
            cronogramaTentativoService.autorizar(id);
            return new ResponseEntity<>(new Mensaje("Cronograma tentativo autorizado.."), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new Mensaje(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (ValidationException e) {
            return new ResponseEntity<>(new Mensaje(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/autorizarUpdate/{id}")
    public ResponseEntity<?> autorizarUpdate(
            @PathVariable("id") Long id,
            @Valid @RequestBody AutorizadoUpdateDto updateDto) {
        cronogramaTentativoService.autorizarUpdate(id, updateDto.getAutorizado(), updateDto);
        return new ResponseEntity<>(new Mensaje("El estado del cronograma tentativo fue actualizado correctamente."),
                HttpStatus.OK);
    }

    // busca cronograma tentativo para comparar con registro de actividad
    @PostMapping("/verificarRegistroIngresoEnTentativo")
    public VerificacionTentativoResponseDto verificarRegistroIngresoEnTentativo(
            @RequestBody RegActivRegIngresoDto dto) {

        return cronogramaTentativoService.verificarRegistroIngresoEnTentativo(dto);
    }

    @PutMapping("/aceptar/{id}")
    public ResponseEntity<?> aceptar(@PathVariable("id") Long id) {

        try {
            cronogramaTentativoService.aceptar(id);
            return new ResponseEntity<>(new Mensaje("Cronograma tentativo aceptado.."), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new Mensaje(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (ValidationException e) {
            return new ResponseEntity<>(new Mensaje(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/listByEfectorAndAutorizado/{idEfector}/{autorizado}")
    public ResponseEntity<List<CronogramaTentativoListAtorizadoDto>> listByEfectorAndAutorizado(
            @PathVariable("idEfector") Long idEfector,
            @PathVariable("autorizado") AutorizadoTentativoEnum autorizado) {

        List<CronogramaTentativoListAtorizadoDto> cronogramas = cronogramaTentativoService
                .findByEfectorIdAndAutorizado(idEfector, autorizado)
                .orElse(new ArrayList<>());

        if (cronogramas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(cronogramas, HttpStatus.OK);
    }

    @GetMapping("/listByAsistencialAndAutorizado/{idAsistencial}/{autorizado}")
    public ResponseEntity<List<CronogramaTentativoListAtorizadoDto>> listByAsistencialAndAutorizado(
            @PathVariable("idAsistencial") Long idAsistencial,
            @PathVariable("autorizado") AutorizadoTentativoEnum autorizado) {

        List<CronogramaTentativoListAtorizadoDto> cronogramas = cronogramaTentativoService
                .findByAsistencialIdAndAutorizado(idAsistencial, autorizado)
                .orElse(new ArrayList<>());

        if (cronogramas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(cronogramas, HttpStatus.OK);
    }

    @GetMapping("/countPendientesByEfector/{idEfector}")
    public ResponseEntity<Long> countPendientesByEfector(
            @PathVariable("idEfector") Long idEfector) {

        Long count = cronogramaTentativoService.countPendientesByEfectorId(idEfector);

        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("/countPendientesByAsistencial/{idAsistencial}")
    public ResponseEntity<Long> countPendientesByAsistencial(
            @PathVariable("idAsistencial") Long idAsistencial) {

        Long count = cronogramaTentativoService.countPendientesByAsistencialId(idAsistencial);

        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("/getServicio/{idTentativo}")
    public ResponseEntity<CronogramaTentativoServicioDto> getTiposGuardias(
            @PathVariable("idTentativo") Long idTentativo) {

        CronogramaTentativoServicioDto servicio = cronogramaTentativoService.obtenerServicio(idTentativo);

        return new ResponseEntity<>(servicio, HttpStatus.OK);
    }

    @PostMapping("/calcularHoraMaximaSalida")
    public ResponseEntity<LocalDateTime> calcularHoraMaximaSalida(
            @RequestBody RegActivRegIngresoDto dto) {
        LocalDateTime horaMaxima = cronogramaTentativoService.calcularHoraMaximaSalida(dto);
        if (horaMaxima != null) {
            return ResponseEntity.ok(horaMaxima);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getIdAutoridadByIdUsuario/{idUsuario}")
    public ResponseEntity<Long> getIdAutoridadByIdUsuario(@PathVariable Long idUsuario) {
        try {
            Long idAutoridad = cronogramaTentativoService.getIdAutoridadByIdUsuario(idUsuario);
            return ResponseEntity.ok(idAutoridad);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/calcularHoraMaximaIngreso")
    public ResponseEntity<LocalDateTime> calcularHoraMaximaEntrada(
            @RequestBody RegActivRegIngresoDto dto) {
        LocalDateTime horaMaxima = cronogramaTentativoService.calcularHoraMaximaIngreso(dto);
        if (horaMaxima != null) {
            return ResponseEntity.ok(horaMaxima);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/getServicioAndTipoGuardia")
    public ResponseEntity<TentativoIdsResponseDto> getServicioAndTipoGuardia(
            @RequestBody TentativoSearchRequestDto request) {
        
        TentativoIdsResponseDto response = cronogramaTentativoService.obtenerIdsCronograma(request);
        return ResponseEntity.ok(response);
    }
}
