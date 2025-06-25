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
import com.guardias.backend.entity.CronogramaDefinitivo;
import com.guardias.backend.entity.RegistroActividad;
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

    @GetMapping("/listAMEcargoyagrup/{anio}/{mes}/{idEfector}")
    public ResponseEntity<List<CronogramaDefinitivo>> listByYearMonthEfectorAndTipoGuardiaCargoReagrupacion(
            @PathVariable("anio") int anio,
            @PathVariable("mes") String mes,
            @PathVariable("idEfector") Long idEfector) {

        MesesEnum mesEnum = MesesEnum.valueOf(mes);

        try {
            List<CronogramaDefinitivo> cronogramasDefinitivos = cronogramaDefinitivoService
                    .findByAnioMesEfectorAndTipoGuardiaCargoReagrupacion(anio, mesEnum, idEfector);

            return new ResponseEntity<List<CronogramaDefinitivo>>(cronogramasDefinitivos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Mensaje("Cronogramas definitivos de Cargo y reagrupacion no encontrados"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/listAMEextra/{anio}/{mes}/{idEfector}")
    public ResponseEntity<List<CronogramaDefinitivo>> listByYearMonthEfectorAndTipoGuardiaExtra(
            @PathVariable("anio") int anio,
            @PathVariable("mes") String mes,
            @PathVariable("idEfector") Long idEfector) {

        MesesEnum mesEnum = MesesEnum.valueOf(mes);

        try {
            List<CronogramaDefinitivo> cronogramasDefinitivos = cronogramaDefinitivoService
                    .findByAnioMesEfectorAndTipoGuardiaExtra(anio, mesEnum, idEfector);

            return new ResponseEntity<List<CronogramaDefinitivo>>(cronogramasDefinitivos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Mensaje("Cronogramas definitivos extra no encontrados"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/listAMEcf/{anio}/{mes}/{idEfector}")
    public ResponseEntity<List<CronogramaDefinitivo>> listByYearMonthEfectorAndTipoGuardiaCF(
            @PathVariable("anio") int anio,
            @PathVariable("mes") String mes,
            @PathVariable("idEfector") Long idEfector) {

        MesesEnum mesEnum = MesesEnum.valueOf(mes);

        try {
            List<CronogramaDefinitivo> cronogramasDefinitivos = cronogramaDefinitivoService
                    .findByAnioMesEfectorAndTipoGuardiaCF(anio, mesEnum, idEfector);

            return new ResponseEntity<List<CronogramaDefinitivo>>(cronogramasDefinitivos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Mensaje("Cronogramas definitivos extra no encontrados"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<CronogramaDefinitivo>> getById(@PathVariable("id") Long id) {
        if (!cronogramaDefinitivoService.activo(id))
            return new ResponseEntity(new Mensaje("El cronograma definitivo no existe"), HttpStatus.NOT_FOUND);
            CronogramaDefinitivo cronogramaDefinitivo = cronogramaDefinitivoService.findById(id).get();
        return new ResponseEntity(cronogramaDefinitivo, HttpStatus.OK);
    }

    @GetMapping("/listMes/{idAsistencial}/{idEfector}/{mes}/{anio}")
    public ResponseEntity<List<RegistroActividad>> getByMes(@PathVariable("idAsistencial") Long idAsistencial,
            @PathVariable("idEfector") Long idEfector,
            @PathVariable("mes") String mes, @PathVariable("anio") int anio) {

        MesesEnum mesEnum = MesesEnum.valueOf(mes);

        try {
            CronogramaDefinitivo cronogramaDefinitivo = cronogramaDefinitivoService
                    .findByAsistencialIdAndEfectorIdAndMesAndAnio(idAsistencial, idEfector, mesEnum, anio)
                    .get();
            List<RegistroActividad> list = cronogramaDefinitivo.getRegistroActividad();
            return new ResponseEntity<List<RegistroActividad>>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Mensaje("Registro no encontrado"), HttpStatus.BAD_REQUEST);
        }
    }

    // @GetMapping("/detailId/{idAsistencial}/{idEfector}/{mes}/{anio}")
    // public ResponseEntity<Long>
    // idByIdAsistencialAndMes(@PathVariable("idAsistencial") Long idAsistencial,
    // @PathVariable("idEfector") Long idEfector,
    // @PathVariable("mes") String mes, @PathVariable("anio") int anio) {
    // MesesEnum mesEnum = MesesEnum.valueOf(mes);

    // try {
    // Long idRegistroMensual = registroMensualService
    // .idByIdAsistencialAndMes(idAsistencial, idEfector, mesEnum, anio)
    // .get();
    // return new ResponseEntity<Long>(idRegistroMensual, HttpStatus.OK);
    // } catch (Exception e) {
    // return new ResponseEntity(new Mensaje("Registro no encontrado"),
    // HttpStatus.BAD_REQUEST);
    // }
    // }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CronogramaDefinitivoDto cronogramaDefinitivoDto) {
        ResponseEntity<?> respuestaValidaciones = cronogramaDefinitivoService.validations(cronogramaDefinitivoDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            CronogramaDefinitivo cronogramaDefinitivo = cronogramaDefinitivoService.createUpdate(new CronogramaDefinitivo(), cronogramaDefinitivoDto);
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

            CronogramaDefinitivo cronogramaDefinitivo = cronogramaDefinitivoService.createUpdate(cronogramaDefinitivoService.findById(id).get(),
                    cronogramaDefinitivoDto);
            cronogramaDefinitivoService.save(cronogramaDefinitivo);
            return new ResponseEntity(new Mensaje("Cronograma definitivo modificado"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    /* public void setRegistroMensual(RegistroActividad registroActividad) {

        Long idAsistencial = registroActividad.getAsistencial().getId();
        Long idEfector = registroActividad.getEfector().getId();
        int mes = registroActividad.getFechaIngreso().getMonth().getValue();
        MesesEnum mesEnum = MesesEnum.fromNumeroMes(mes);
        int anio = registroActividad.getFechaIngreso().getYear();
        Long id;

        try {
            RegistroMensual registroMensual = registroMensualService
                    .findByAsistencialIdAndEfectorIdAndMesAndAnio(idAsistencial, idEfector, mesEnum, anio)
                    .get();
            id = registroMensual.getId();
        } catch (Exception exception) {
            System.out.println("id no encontrado");
            id = createRegistroMensual(idAsistencial, idEfector, mesEnum, anio);
        }

        try {
            registroActividad.setRegistroMensual(registroMensualService.findById(id).get());
            registroActividadService.save(registroActividad);
        } catch (Exception e) {
            System.out.println("error: idRegistroMensual nulo -- " + e.getMessage());
        }
    }
 */
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
}
