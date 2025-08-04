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
import com.guardias.backend.dto.registroActividad.RegActivMotivoDto;
import com.guardias.backend.dto.registroActividad.RegActivNombresDto;
import com.guardias.backend.dto.registroActividad.RegActivRegSalidaDto;
import com.guardias.backend.entity.RegistroActividad;
import com.guardias.backend.security.service.UsuarioService;
import com.guardias.backend.service.AsistencialService;
import com.guardias.backend.service.EfectorService;
import com.guardias.backend.service.RegistroActividadService;
import com.guardias.backend.service.RegistroMensualService;
import com.guardias.backend.service.RegistrosPendientesService;
import com.guardias.backend.service.ServicioService;
import com.guardias.backend.service.TipoGuardiaService;

@RestController
@RequestMapping("/registroActividad")
@CrossOrigin(origins = "http://localhost:4200")
public class RegistroActividadController {

    @Autowired
    RegistroActividadService registroActividadService;
    @Autowired
    ServicioService servicioService;
    @Autowired
    EfectorService efectorService;
    @Autowired
    AsistencialService asistencialService;
    @Autowired
    TipoGuardiaService tipoGuardiaService;

    @Autowired
    RegistroMensualService registroMensualService;
    @Autowired
    RegistroMensualController registroMensualController;
    @Autowired
    EfectorController efectorController;
    @Autowired
    RegistrosPendientesController registrosPendientesController;
    @Autowired
    RegistrosPendientesService registrosPendientesService;
    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/list")
    public ResponseEntity<List<RegistroActividad>> list() {
        List<RegistroActividad> list = registroActividadService.findByActivoTrue().get();
        return new ResponseEntity<List<RegistroActividad>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<RegistroActividad>> listAll() {
        List<RegistroActividad> list = registroActividadService.findAll();
        return new ResponseEntity<List<RegistroActividad>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<RegistroActividad>> getById(@PathVariable("id") Long id) {
        if (!registroActividadService.activo(id))
            return new ResponseEntity(new Mensaje("El registro de actividad no existe"), HttpStatus.NOT_FOUND);
        RegistroActividad registroActividad = registroActividadService.findById(id).get();
        return new ResponseEntity(registroActividad, HttpStatus.OK);
    }

    /*
     * public ResponseEntity<?> validations(RegistroActividadDto
     * registroActividadDto) {
     * 
     * if (registroActividadDto.getFechaIngreso() == null)
     * return new ResponseEntity(new Mensaje("la fecha de ingreso es obligatoria"),
     * HttpStatus.BAD_REQUEST);
     * 
     * if (registroActividadDto.getHoraIngreso() == null)
     * return new ResponseEntity(new Mensaje("la hora de ingreso es obligatoria"),
     * HttpStatus.BAD_REQUEST);
     * 
     * return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
     * }
     */

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody RegistroActividadDto registroActividadDto) {

        ResponseEntity<?> respuestaValidaciones = registroActividadService.validations(registroActividadDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            RegistroActividad registroActividad = registroActividadService.createUpdate(new RegistroActividad(),
                    registroActividadDto);

            registroActividad = registrosPendientesService.addRegistroActividad(registroActividad);
            registroActividadService.save(registroActividad);

            if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
                return new ResponseEntity(new Mensaje("Registro de Actividad creado"), HttpStatus.OK);
            } else {
                return new ResponseEntity(new Mensaje("error"), HttpStatus.BAD_REQUEST);
            }
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id,
            @RequestBody RegistroActividadDto registroActividadDto) {
        if (!registroActividadService.activo(id))
            return new ResponseEntity(new Mensaje("Registro de actividad no existe"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = registroActividadService.validations(registroActividadDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            RegistroActividad registroActividad = registroActividadService.createUpdate(
                    registroActividadService.findById(id).get(),
                    registroActividadDto);
            registroActividadService.save(registroActividad);
            return new ResponseEntity(new Mensaje("Registro de Actividad modificada"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    // VER que tipo de registro recibirá desde el front para la salida
    @PutMapping("/registrarSalida/{id}")
    public ResponseEntity<?> registrarSalida(@PathVariable("id") Long id,
            @RequestBody RegistroActividadDto registroActividadDto) {

        if (!registroActividadService.activo(id))
            return new ResponseEntity(new Mensaje("Registro de actividad no existe"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> registrarSalida = registroActividadService.registrarSalida(id, registroActividadDto);

        return registrarSalida;
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!registroActividadService.activo(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        RegistroActividad registroActividad = registroActividadService.findById(id).get();
        registroActividad.setActivo(false);
        registroActividadService.save(registroActividad);

        return new ResponseEntity<>(new Mensaje("Registro de actividad eliminada correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!registroActividadService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        registroActividadService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("Registro de actividad eliminada FISICAMENTEE"), HttpStatus.OK);
    }

    @GetMapping("/getRegActivPendiente/{idAsistencial}/{idEfector}")
    public ResponseEntity<RegActivRegSalidaDto> getRegActivPendiente(@PathVariable("idAsistencial") Long idAsistencial,
            @PathVariable("idEfector") Long idEfector) {

        RegActivRegSalidaDto dto = registrosPendientesService
                .obtenerRegistroPendienteDto(idAsistencial, idEfector);

        return dto != null
                ? ResponseEntity.ok(dto)
                : ResponseEntity.noContent().build();
    }

    @GetMapping("/listRegActivPendienteByEfector/{idEfector}")
    public ResponseEntity<List<RegActivNombresDto>> listRegActivPendienteByEfector(
            @PathVariable("idEfector") Long idEfector) {
        List<RegActivNombresDto> list = registrosPendientesService.listarRegistrosPendientesPorEfector(idEfector);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/listMotivo/{idEfector}/{idServicio}/{mes}/{anio}")
    public ResponseEntity<List<RegActivMotivoDto>> listMotivo(
            @PathVariable("idEfector") Long idEfector,
            @PathVariable("idServicio") Long idServicio,
            @PathVariable("mes") int mes,
            @PathVariable("anio") int anio) {

        List<RegActivMotivoDto> list = registroActividadService.listarMotivos(idEfector, mes, anio, idServicio);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/listMotivoByAsistencial/{idAsistencial}/{idEfector}/{idServicio}/{mes}/{anio}")
    public ResponseEntity<List<RegActivMotivoDto>> listMotivoByAsistencial(
            @PathVariable("idAsistencial") Long idAsistencial,
            @PathVariable("idEfector") Long idEfector,
            @PathVariable("idServicio") Long idServicio,
            @PathVariable("mes") int mes,
            @PathVariable("anio") int anio) {

        List<RegActivMotivoDto> list = registroActividadService
                .listarMotivosByAsistencial(idAsistencial, idEfector, mes, anio, idServicio);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/validar-precondiciones-cronograma/{idEfector}/{mes}/{anio}")
    public ResponseEntity<Boolean> checkCompleteDdjjSet(
            @PathVariable Long idEfector,
            @PathVariable int mes,
            @PathVariable int anio) {

        boolean existsCompleteSet = registroActividadService.validarPrecondicionesCronograma(idEfector, mes, anio);
        return new ResponseEntity<>(existsCompleteSet, HttpStatus.OK);
    }

    @GetMapping("/obtener-ddjj-aprobadas/{idEfector}/{mes}/{anio}")
    public ResponseEntity<List<Long>> obtenerDdjjAprobadas(
            @PathVariable Long idEfector,
            @PathVariable int mes,
            @PathVariable int anio) {

        List<Long> ddjjAprobadas = registroActividadService.obtenerIdsDdjjAprobadas(idEfector, mes, anio);
        return new ResponseEntity<>(ddjjAprobadas, HttpStatus.OK);
    }

}
