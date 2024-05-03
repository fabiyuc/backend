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
import com.guardias.backend.dto.RegistroMensualDto;
import com.guardias.backend.entity.Efector;
import com.guardias.backend.entity.RegistroActividad;
import com.guardias.backend.entity.TipoGuardia;
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.service.AsistencialService;
import com.guardias.backend.service.EfectorService;
import com.guardias.backend.service.RegistroActividadService;
import com.guardias.backend.service.RegistroMensualService;
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

        if (registroActividadDto.getHoraIngreso() == null)
            return new ResponseEntity(new Mensaje("la hora de ingreso es obligatoria"),
                    HttpStatus.BAD_REQUEST);

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
            registroActividad
                    .setTipoGuardia(tipoGuardiaService.findById(registroActividadDto.getIdTipoGuardia()).get());
        }

        if (registroActividad.getFechaIngreso() != registroActividadDto.getFechaIngreso() &&
                registroActividadDto.getFechaIngreso() != null)
            registroActividad.setFechaIngreso(registroActividadDto.getFechaIngreso());

        if (registroActividad.getFechaEgreso() != registroActividadDto.getFechaEgreso() &&
                registroActividadDto.getFechaEgreso() != null)
            registroActividad.setFechaEgreso(registroActividadDto.getFechaEgreso());

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

        if (registroActividadDto.getIdRegistroMensual() != null && (registroActividad.getRegistroMensual() == null
                || !Objects.equals(registroActividad.getRegistroMensual().getId(),
                        registroActividadDto.getIdRegistroMensual()))) {
            registroActividad.setRegistroMensual(
                    registroMensualService.findById(registroActividadDto.getIdRegistroMensual()).get());
        }

        registroActividad.setActivo(true);
        return registroActividad;
    }

    public Long createRegistroMensual(Long idAsistencial, Long idEfector, MesesEnum mesEnum, int anio) {
        RegistroMensualDto registroMensualDto = new RegistroMensualDto();

        registroMensualDto.setMes(mesEnum);
        registroMensualDto.setAnio(anio);
        registroMensualDto.setIdAsistencial(idAsistencial);
        registroMensualDto.setIdEfector(idEfector);

        ResponseEntity<?> respuesta = registroMensualController.create(registroMensualDto);

        if (respuesta.getStatusCode() == HttpStatus.OK) {
            return registroMensualController.idByIdAsistencialAndMes(idAsistencial, idEfector, mesEnum.toString(), anio)
                    .getBody();
        } else {
            return null;
        }
    }

    public void setRegistroMensual(RegistroActividad registroActividad) {

        Long idAsistencial = registroActividad.getAsistencial().getId();
        Long idEfector = registroActividad.getEfector().getId();
        int mes = registroActividad.getFechaIngreso().getMonth().getValue();
        MesesEnum mesEnum = MesesEnum.fromNumeroMes(mes);
        int anio = registroActividad.getFechaIngreso().getYear();
        Long id;
        try {
            id = registroMensualController.idByIdAsistencialAndMes(idAsistencial, idEfector, mesEnum.toString(), anio)
                    .getBody();
        } catch (Exception e) {
            id = createRegistroMensual(idAsistencial, idEfector, mesEnum, anio);
        }

        try {
            registroActividad.setRegistroMensual(registroMensualService.findById(id).get());
            registroActividadService.save(registroActividad);
        } catch (Exception e) {
            System.out.println("error: idRegistroMensual nulo -- " + e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody RegistroActividadDto registroActividadDto) {

        ResponseEntity<?> respuestaValidaciones = validations(registroActividadDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            RegistroActividad registroActividad = createUpdate(new RegistroActividad(), registroActividadDto);
            registroActividadService.save(registroActividad);
            setRegistroMensual(registroActividad);
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

        //pruebo actualizar la lista de registroActiv en efectores pero da error ClassCastException
        /* Efector efector = registroActividad.getEfector();
        efector.getRegistroActividad().remove(registroActividad);
        efectorService.saveEfector(efector); */


        // modifica la lista pero no guarda actualizada
        /* TipoGuardia tipoGuardia = registroActividad.getTipoGuardia();

        System.out.println("Datos en la lista antes del remove:");
    for (RegistroActividad ra : tipoGuardia.getRegistrosActividades()) {
        System.out.println("registro    : "+ ra.getId());
    }

        TipoGuardia tipoGuardiaModificado = tipoGuardiaService.findById(tipoGuardia.getId()).orElse(null);

        tipoGuardiaModificado.getRegistrosActividades().remove(registroActividad);
        
        System.out.println("Datos en la lista despues del remove:");
        for (RegistroActividad ra : tipoGuardiaModificado.getRegistrosActividades()) {
            System.out.println("registro    "+ ra.getId());
        }

        tipoGuardiaService.save(tipoGuardiaModificado); */

        return new ResponseEntity<>(new Mensaje("Registro de actividad eliminada correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!registroActividadService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        registroActividadService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("Registro de actividad eliminada FISICAMENTEE"), HttpStatus.OK);
    }

}
