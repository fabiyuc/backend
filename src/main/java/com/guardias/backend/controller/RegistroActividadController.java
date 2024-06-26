package com.guardias.backend.controller;

import java.util.List;
import java.util.Objects;

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
import com.guardias.backend.entity.RegistroActividad;
import com.guardias.backend.service.AsistencialService;
import com.guardias.backend.service.EfectorService;
import com.guardias.backend.service.RegistroActividadService;
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

    @GetMapping("/list")
    public ResponseEntity<List<RegistroActividad>> list() {
        List<RegistroActividad> list = registroActividadService.findByActivo();
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

    public ResponseEntity<?> validations(@RequestBody RegistroActividadDto registroActividadDto) {

        if (registroActividadDto.getFechaIngreso() == null)
            return new ResponseEntity(new Mensaje("la fecha de ingreso es obligatoria"), HttpStatus.BAD_REQUEST);

        /* if (registroActividadDto.getFechaEgreso() == null)
            return new ResponseEntity(new Mensaje("la fecha de egreso es obligatoria"), HttpStatus.BAD_REQUEST);

        if (registroActividadDto.getHoraIngreso() == null)
            return new ResponseEntity(new Mensaje("la hora de ingreso es obligatoria"), HttpStatus.BAD_REQUEST); */

        if (registroActividadDto.getHoraEgreso() == null)
            return new ResponseEntity(new Mensaje("la hora de egreso es obligatoria"), HttpStatus.BAD_REQUEST);
        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    private RegistroActividad createUpdate(RegistroActividad registroActividad,
            RegistroActividadDto registroActividadDto) {

        if (registroActividad.getServicio() == null ||
                (registroActividadDto.getIdServicio() != null &&
                        !Objects.equals(registroActividad.getServicio().getId(),
                                registroActividadDto.getIdServicio()))) {
            registroActividad.setServicio(servicioService.findById(registroActividadDto.getIdServicio()).get());
        }

        if (registroActividad.getTipoGuardia() == null ||
                (registroActividadDto.getIdTipoGuardia() != null &&
                        !Objects.equals(registroActividad.getTipoGuardia().getId(),
                                registroActividadDto.getIdTipoGuardia()))) {
            registroActividad.setTipoGuardia(tipoGuardiaService.findById(registroActividadDto.getIdTipoGuardia()).get());
        }

        if (registroActividad.getFechaIngreso() != registroActividadDto.getFechaIngreso() &&
                registroActividadDto.getFechaIngreso() != null)
            registroActividad.setFechaIngreso(registroActividadDto.getFechaIngreso());

        if (registroActividad.getFechaEgreso() != registroActividadDto.getFechaEgreso() &&
                registroActividadDto.getFechaEgreso() != null)
            registroActividad.setFechaIngreso(registroActividadDto.getFechaEgreso());

        if (registroActividad.getHoraIngreso() != registroActividadDto.getHoraIngreso() &&
                registroActividadDto.getHoraIngreso() != null)
            registroActividad.setHoraIngreso(registroActividadDto.getHoraIngreso());

        if (registroActividad.getHoraEgreso() != registroActividadDto.getHoraEgreso() &&
                registroActividadDto.getHoraEgreso() != null)
            registroActividad.setHoraEgreso(registroActividadDto.getHoraEgreso());

        if (registroActividad.getAsistencial() == null ||
                (registroActividadDto.getIdAsistencial() != null &&
                        !Objects.equals(registroActividad.getAsistencial().getId(),
                                registroActividadDto.getIdAsistencial()))) {
            registroActividad
                    .setAsistencial(asistencialService.findById(registroActividadDto.getIdAsistencial()).get());
        }

        if (registroActividad.getEfector() == null ||
                (registroActividadDto.getIdEfector() != null &&
                        !Objects.equals(registroActividad.getEfector().getId(),
                                registroActividadDto.getIdEfector()))) {
            registroActividad.setEfector(efectorService.findById(registroActividadDto.getIdEfector()));
        }

        registroActividad.setActivo(true);
        return registroActividad;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody RegistroActividadDto registroActividadDto) {

        ResponseEntity<?> respuestaValidaciones = validations(registroActividadDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            RegistroActividad registroActividad = createUpdate(new RegistroActividad(), registroActividadDto);
            registroActividadService.save(registroActividad);
            return new ResponseEntity(new Mensaje("Registro de Actividad creado"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id,
            @RequestBody RegistroActividadDto registroActividadDto) {
        if (!registroActividadService.activo(id))
            return new ResponseEntity(new Mensaje("Registro de actividad no existe"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = validations(registroActividadDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            RegistroActividad registroActividad = createUpdate(registroActividadService.findById(id).get(),
                    registroActividadDto);
            registroActividadService.save(registroActividad);
            return new ResponseEntity(new Mensaje("Registro de Actividad modificada"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!registroActividadService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        RegistroActividad registroActividad = registroActividadService.findById(id).get();
        registroActividad.setActivo(false);
        registroActividadService.save(registroActividad);
        return new ResponseEntity<>(new Mensaje("Notificación eliminada correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!registroActividadService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        registroActividadService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("Notificación eliminada FISICAMENTEE"), HttpStatus.OK);
    }

}
