package com.guardias.backend.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.RegistroMensualDto;
import com.guardias.backend.dto.registroMensual.RegistroMensualListDto;
import com.guardias.backend.entity.RegistroActividad;
import com.guardias.backend.entity.RegistroMensual;
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.enums.QuincenaEnum;
import com.guardias.backend.service.AsistencialService;
import com.guardias.backend.service.DdjjService;
import com.guardias.backend.service.EfectorService;
import com.guardias.backend.service.RegistroActividadService;
import com.guardias.backend.service.RegistroMensualService;
import com.guardias.backend.service.SumaHorasService;

@RestController
@RequestMapping("/registroMensual")
@CrossOrigin(origins = "http://localhost:4200")
public class RegistroMensualController {
    @Autowired
    RegistroMensualService registroMensualService;
    @Autowired
    EfectorService efectorService;
    @Autowired
    RegistroActividadService registroActividadService;
    @Autowired
    DdjjService ddjjService;
    @Autowired
    AsistencialService asistencialService;
    @Autowired
    SumaHorasService sumaHorasService;

    @GetMapping("/list")
    public ResponseEntity<List<RegistroMensual>> list() {
        List<RegistroMensual> list = registroMensualService.findByActivoTrue().get();
        return new ResponseEntity<List<RegistroMensual>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<RegistroMensual>> listAll() {
        List<RegistroMensual> list = registroMensualService.findAll();
        return new ResponseEntity<List<RegistroMensual>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAMEcargoyagrup/{anio}/{mes}/{idEfector}")
    public ResponseEntity<List<RegistroMensual>> listByYearMonthEfectorAndTipoGuardiaCargoReagrupacion(
            @PathVariable("anio") int anio,
            @PathVariable("mes") String mes,
            @PathVariable("idEfector") Long idEfector) {

        MesesEnum mesEnum = MesesEnum.valueOf(mes);

        try {
            List<RegistroMensual> registrosMensuales = registroMensualService
                    .findByAnioMesEfectorAndTipoGuardiaCargoReagrupacion(anio, mesEnum, idEfector);

            return new ResponseEntity<>(registrosMensuales, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Mensaje("Registros mensuales de Cargo y reagrupación no encontrados"),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/listAMEcargoyagrupAndServicio/{anio}/{mes}/{idEfector}/{idServicio}")
    public ResponseEntity<List<RegistroMensual>> listByYearMonthEfectorAndTipoGuardiaCargoReagrupacionAndServicio(
            @PathVariable("anio") int anio,
            @PathVariable("mes") String mes,
            @PathVariable("idEfector") Long idEfector,
            @PathVariable("idServicio") Long idServicio) {

        MesesEnum mesEnum = MesesEnum.valueOf(mes);

        try {
            List<RegistroMensual> registrosMensuales = registroMensualService
                    .findByAnioMesEfectorAndTipoGuardiaCargoReagrupacionAndServicio(anio, mesEnum, idEfector,
                            idServicio);

            return new ResponseEntity<>(registrosMensuales, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(
                    new Mensaje("Registros mensuales de Cargo y reagrupación no encontrados"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/listAMEextra/{anio}/{mes}/{idEfector}")
    public ResponseEntity<List<RegistroMensual>> listByYearMonthEfectorAndTipoGuardiaExtra(
            @PathVariable("anio") int anio,
            @PathVariable("mes") String mes,
            @PathVariable("idEfector") Long idEfector) {

        MesesEnum mesEnum = MesesEnum.valueOf(mes);

        try {
            List<RegistroMensual> registrosMensuales = registroMensualService
                    .findByAnioMesEfectorAndTipoGuardiaExtra(anio, mesEnum, idEfector);

            return new ResponseEntity<>(registrosMensuales, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Mensaje("Registros mensuales extra no encontrados"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/listAMEextraAndServicio/{anio}/{mes}/{idEfector}/{idServicio}")
    public ResponseEntity<List<RegistroMensual>> listByYearMonthEfectorAndTipoGuardiaExtraAndServicio(
            @PathVariable("anio") int anio,
            @PathVariable("mes") String mes,
            @PathVariable("idEfector") Long idEfector,
            @PathVariable("idServicio") Long idServicio) {

        MesesEnum mesEnum = MesesEnum.valueOf(mes);

        try {
            List<RegistroMensual> registrosMensuales = registroMensualService
                    .findByAnioMesEfectorAndTipoGuardiaExtraAndServicio(anio, mesEnum, idEfector,
                            idServicio);

            return new ResponseEntity<>(registrosMensuales, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(
                    new Mensaje("Registros mensuales de extra no encontrados"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/listAMEcf/{anio}/{mes}/{idEfector}")
    public ResponseEntity<List<RegistroMensual>> listByYearMonthEfectorAndTipoGuardiaCF(
            @PathVariable("anio") int anio,
            @PathVariable("mes") String mes,
            @PathVariable("idEfector") Long idEfector) {

        MesesEnum mesEnum = MesesEnum.valueOf(mes);

        try {
            List<RegistroMensual> registrosMensuales = registroMensualService
                    .findByAnioMesEfectorAndTipoGuardiaCF(anio, mesEnum, idEfector);

            return new ResponseEntity<>(registrosMensuales, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Mensaje("Registros mensuales extra no encontrados"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/listAMEcfAndServicio/{anio}/{mes}/{idEfector}/{idServicio}")
    public ResponseEntity<List<RegistroMensual>> listByYearMonthEfectorAndTipoGuardiaCFAndServicio(
            @PathVariable("anio") int anio,
            @PathVariable("mes") String mes,
            @PathVariable("idEfector") Long idEfector,
            @PathVariable("idServicio") Long idServicio) {

        MesesEnum mesEnum = MesesEnum.valueOf(mes);

        try {
            List<RegistroMensual> registrosMensuales = registroMensualService
                    .findByAnioMesEfectorAndTipoGuardiaCFAndServicio(anio, mesEnum, idEfector,
                            idServicio);

            return new ResponseEntity<>(registrosMensuales, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(
                    new Mensaje("Registros mensuales de Cf no encontrados"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<RegistroMensual>> getById(@PathVariable("id") Long id) {
        if (!registroMensualService.activo(id))
            return new ResponseEntity(new Mensaje("El registro mensual no existe"), HttpStatus.NOT_FOUND);
        RegistroMensual registroMensual = registroMensualService.findById(id).get();
        return new ResponseEntity(registroMensual, HttpStatus.OK);
    }

    @GetMapping("/listMes/{idAsistencial}/{idEfector}/{mes}/{anio}")
    public ResponseEntity<List<RegistroActividad>> getByMes(@PathVariable("idAsistencial") Long idAsistencial,
            @PathVariable("idEfector") Long idEfector,
            @PathVariable("mes") String mes, @PathVariable("anio") int anio) {

        MesesEnum mesEnum = MesesEnum.valueOf(mes);

        try {
            RegistroMensual registroMensual = registroMensualService
                    .findByAsistencialIdAndEfectorIdAndMesAndAnio(idAsistencial, idEfector, mesEnum, anio)
                    .get();
            List<RegistroActividad> list = registroMensual.getRegistroActividad();
            return new ResponseEntity<List<RegistroActividad>>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Mensaje("Registro no encontrado"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody RegistroMensualDto registroMensualDto) {
        ResponseEntity<?> respuestaValidaciones = registroMensualService.validations(registroMensualDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            RegistroMensual registroMensual = registroMensualService.createUpdate(new RegistroMensual(),
                    registroMensualDto);
            registroMensualService.save(registroMensual);
            return new ResponseEntity(new Mensaje("Registro de Actividad creado"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id,
            @RequestBody RegistroMensualDto registroMensualDto) {
        if (!registroMensualService.activo(id))
            return new ResponseEntity(new Mensaje("Registro de actividad no existe"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = registroMensualService.validations(registroMensualDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            RegistroMensual registroMensual = registroMensualService.createUpdate(
                    registroMensualService.findById(id).get(),
                    registroMensualDto);
            registroMensualService.save(registroMensual);
            return new ResponseEntity(new Mensaje("Registro de Actividad modificado"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!registroMensualService.activo(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        RegistroMensual registroMensual = registroMensualService.findById(id).get();
        registroMensual.setActivo(false);
        registroMensualService.save(registroMensual);
        return new ResponseEntity<>(new Mensaje("Registro mensual eliminado correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!registroMensualService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        registroMensualService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("Registro mensual eliminado FISICAMENTEE"), HttpStatus.OK);
    }

    @GetMapping("/listCargoyagrupAndServicio/{anio}/{mes}/{idEfector}/{idServicio}")
    public ResponseEntity<List<RegistroMensualListDto>> listByTipoGuardiaCargoReagrupacionAndServicio(
            @PathVariable("anio") int anio,
            @PathVariable("mes") String mes,
            @PathVariable("idEfector") Long idEfector,
            @PathVariable("idServicio") Long idServicio) {

        MesesEnum mesEnum = MesesEnum.valueOf(mes);

        try {
            List<RegistroMensualListDto> registros = registroMensualService
                    .findByTipoGuardiaCargoReagrupacionAndServicio(anio, mesEnum, idEfector,
                            idServicio);

            return new ResponseEntity<>(registros, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(
                    new Mensaje("Registros mensuales de Cargo y reagrupación no encontrados"),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/listCargoyagrup/{anio}/{mes}/{idEfector}")
    public ResponseEntity<List<RegistroMensualListDto>> listByTipoGuardiaCargoReagrupacion(
            @PathVariable("anio") int anio,
            @PathVariable("mes") String mes,
            @PathVariable("idEfector") Long idEfector) {

        MesesEnum mesEnum = MesesEnum.valueOf(mes);

        try {
            List<RegistroMensualListDto> registros = registroMensualService
                    .findByTipoGuardiaCargoReagrupacion(anio, mesEnum, idEfector);

            return new ResponseEntity<>(registros, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(
                    new Mensaje("Registros mensuales de Cargo y reagrupación no encontrados"),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/listExtraAndServicio/{anio}/{mes}/{idEfector}/{idServicio}")
    public ResponseEntity<List<RegistroMensualListDto>> listByTipoGuardiaExtraAndServicio(
            @PathVariable("anio") int anio,
            @PathVariable("mes") String mes,
            @PathVariable("idEfector") Long idEfector,
            @PathVariable("idServicio") Long idServicio) {

        MesesEnum mesEnum = MesesEnum.valueOf(mes);

        try {
            List<RegistroMensualListDto> registros = registroMensualService
                    .findByTipoGuardiaExtraAndServicio(anio, mesEnum, idEfector,
                            idServicio);

            return new ResponseEntity<>(registros, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(
                    new Mensaje("Registros mensuales de extra no encontrados"),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/listExtra/{anio}/{mes}/{idEfector}")
    public ResponseEntity<List<RegistroMensualListDto>> listByTipoGuardiaExtra(
            @PathVariable("anio") int anio,
            @PathVariable("mes") String mes,
            @PathVariable("idEfector") Long idEfector) {

        MesesEnum mesEnum = MesesEnum.valueOf(mes);

        try {
            List<RegistroMensualListDto> registros = registroMensualService
                    .findByTipoGuardiaExtra(anio, mesEnum, idEfector);

            return new ResponseEntity<>(registros, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(
                    new Mensaje("Registros mensuales de extra no encontrados"),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/listCfAndServicio/{anio}/{mes}/{idEfector}/{idServicio}/{quincena}")
    public ResponseEntity<List<RegistroMensualListDto>> listByTipoGuardiaCfAndServicio(
            @PathVariable("anio") int anio,
            @PathVariable("mes") String mes,
            @PathVariable("idEfector") Long idEfector,
            @PathVariable("idServicio") Long idServicio,
            @PathVariable("quincena") String quincena) {

        MesesEnum mesEnum = MesesEnum.valueOf(mes);
        QuincenaEnum quincenaEnum = QuincenaEnum.valueOf(quincena.toUpperCase());

        try {
            List<RegistroMensualListDto> registros = registroMensualService
                    .findByTipoGuardiaCfAndServicio(anio, mesEnum, idEfector,
                            idServicio, quincenaEnum);

            return new ResponseEntity<>(registros, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(
                    new Mensaje("Registros mensuales de CF no encontrados"),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/listCf/{anio}/{mes}/{idEfector}/{quincena}")
    public ResponseEntity<List<RegistroMensualListDto>> listByTipoGuardiaCf(
            @PathVariable("anio") int anio,
            @PathVariable("mes") String mes,
            @PathVariable("idEfector") Long idEfector,
            @PathVariable("quincena") String quincena) {

        MesesEnum mesEnum = MesesEnum.valueOf(mes);
        QuincenaEnum quincenaEnum = QuincenaEnum.valueOf(quincena.toUpperCase());

        try {
            List<RegistroMensualListDto> registros = registroMensualService
                    .findByTipoGuardiaCf(anio, mesEnum, idEfector, quincenaEnum);

            return new ResponseEntity<>(registros, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Mensaje("Registros mensuales de CF no encontrados"),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getMontoTotalByQuincena/{idAsistencial}/{idEfector}/{quincena}/{mes}/{anio}")
    public ResponseEntity<?> getMontoTotalByQuincena(
            @PathVariable("idAsistencial") Long idAsistencial,
            @PathVariable("idEfector") Long idEfector,
            @PathVariable("quincena") String quincena,
            @PathVariable("mes") String mes,
            @PathVariable("anio") int anio) {

        QuincenaEnum quincenaEnum = QuincenaEnum.valueOf(quincena.toUpperCase());
        MesesEnum mesEnum = MesesEnum.valueOf(mes.toUpperCase());

        try {
            BigDecimal monto = registroMensualService.getMontoTotalByQuincena(idAsistencial, idEfector, quincenaEnum,
                    mesEnum, anio);

            return new ResponseEntity<>(monto, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new Mensaje("Error al obtener el monto " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/incompletos/{idEfector}/{mes}/{anio}/{quincena}")
    public ResponseEntity<List<RegistroMensualListDto>> getRegistrosIncompletos(
            @PathVariable Long idEfector,
            @PathVariable String mes,
            @PathVariable int anio,
            @PathVariable String quincena) {

        try {
            MesesEnum mesEnum = MesesEnum.valueOf(mes);
            QuincenaEnum quincenaEnum = QuincenaEnum.valueOf(quincena.toUpperCase());
            List<RegistroMensualListDto> registros = registroMensualService.findRegistrosIncompletos(idEfector, mesEnum,
                    anio, quincenaEnum);

            return ResponseEntity.ok(registros);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }

    @GetMapping("/fuera-de-termino/{idEfector}/{mes}/{anio}")
    public ResponseEntity<List<RegistroMensualListDto>> listFueraDeTermino(
            @PathVariable Long idEfector,
            @PathVariable String mes,
            @PathVariable int anio) {

        try {
            MesesEnum mesEnum = MesesEnum.valueOf(mes);
            List<RegistroMensualListDto> registros = registroMensualService.findRegistrosFueraDeTermino(idEfector,
                    mesEnum, anio);

            return ResponseEntity.ok(registros);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }

    @GetMapping("/fuera-de-termino-por-servicio/{idEfector}/{mes}/{anio}/{idServicio}")
    public ResponseEntity<List<RegistroMensualListDto>> listFueraDeTermino(
            @PathVariable Long idEfector,
            @PathVariable String mes,
            @PathVariable int anio,
            @PathVariable Long idServicio) {

        try {
            MesesEnum mesEnum = MesesEnum.valueOf(mes);
            List<RegistroMensualListDto> registros = registroMensualService.findRegistrosFueraDeTerminoPorServicio(idEfector,
                    mesEnum, anio, idServicio);

            return ResponseEntity.ok(registros);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }

    @GetMapping("/existen-fuera-de-termino/{idEfector}")
    public ResponseEntity<Boolean> existenRegistrosFueraDeTermino(
            @PathVariable Long idEfector,
            @PathVariable LocalDate fechaActual) {

        try {
            boolean existen = registroMensualService.existenRegistrosFueraDeTermino(idEfector, fechaActual);
            return ResponseEntity.ok(existen);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    @GetMapping("/existen-completos/{idEfector}/{quincena}")
    public ResponseEntity<Boolean> existenAutorizados(
            @PathVariable Long idEfector,
             @PathVariable String mes,
            @PathVariable int anio,
            @PathVariable String quincena) {

        try {
            MesesEnum mesEnum = MesesEnum.valueOf(mes);
            QuincenaEnum quincenaEnum = QuincenaEnum.valueOf(quincena.toUpperCase());
            boolean existen = registroMensualService.existenCompletos(idEfector, mesEnum, anio, quincenaEnum);
            return ResponseEntity.ok(existen);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

}
